package com.sparkshard.chaos.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EntityFBI extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    // Tracking state for the kick animation
    private int kickTicks = 0;

    public EntityFBI(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // Priority 0: Don't drown in the 20x20 lake
        this.goalSelector.add(0, new SwimGoal(this));
        
        // Priority 1: The "Exterminate" behavior (Tracks players across the 500x500 map)
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5D, false));
        
        // Priority 2: Standard movement
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        
        // Target players immediately
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        // DOOR KICKING LOGIC
        // Checks if there is a door in front of the agent
        if (!this.getWorld().isClient) {
            BlockPos frontPos = this.getBlockPos().offset(this.getHorizontalFacing());
            BlockState state = this.getWorld().getBlockState(frontPos);

            if (state.isIn(BlockTags.WOODEN_DOORS) || state.isIn(BlockTags.DOORS)) {
                startKickAnimation();
                // Break the door after a short delay (simulating the kick impact)
                if (this.kickTicks == 10) {
                    this.getWorld().breakBlock(frontPos, false);
                }
            }
        }

        // ATTACK LOGIC
        // If the agent is touching a player, trigger the kick animation to "attack"
        if (this.getTarget() instanceof PlayerEntity && this.distanceTo(this.getTarget()) < 2.0F) {
            startKickAnimation();
        }

        if (this.kickTicks > 0) {
            this.kickTicks--;
        }
    }

    private void startKickAnimation() {
        if (this.kickTicks <= 0) {
            this.kickTicks = 20; // Matches length of animation
            this.getWorld().sendEntityStatus(this, (byte) 60); // Custom swing trigger
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> {
            // Check if we are currently in a "Kick" state
            if (this.kickTicks > 0) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.fbi.attack"));
            }
            
            // Movement animations
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }
            
            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
