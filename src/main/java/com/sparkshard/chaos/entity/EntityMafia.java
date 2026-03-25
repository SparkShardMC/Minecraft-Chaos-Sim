package com.sparkshard.chaos.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class EntityMafia extends HostileEntity implements GeoEntity, RangedAttackMob {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public enum MafiaType { SWORDSMAN, AXEMAN, ARCHER }
    private MafiaType type = MafiaType.SWORDSMAN;

    public EntityMafia(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        // Randomly assign type on spawn
        int roll = this.random.nextInt(3);
        if (roll == 0) this.type = MafiaType.SWORDSMAN;
        else if (roll == 1) this.type = MafiaType.AXEMAN;
        else this.type = MafiaType.ARCHER;
        
        this.setEquipment();
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    private void setEquipment() {
        this.clearStatusEffects();
        switch (this.type) {
            case SWORDSMAN -> {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
            }
            case AXEMAN -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
            case ARCHER -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, EntityFBI.class, true));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

        if (this.type == MafiaType.ARCHER) {
            this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0D, 40, 15.0F));
        } else {
            this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        }
    }

    // --- REVENGE BEACON LOGIC ---
    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (!this.getWorld().isClient) {
            // Play Whistle Sound at death location
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.HOSTILE, 1.0f, 1.0f);
            
            // Alert all Mafia within 100 blocks
            Box box = new Box(this.getBlockPos()).expand(100);
            List<EntityMafia> nearbyMafia = this.getWorld().getEntitiesByClass(EntityMafia.class, box, entity -> entity != this);
            
            for (EntityMafia mafia : nearbyMafia) {
                if (damageSource.getAttacker() instanceof LivingEntity attacker) {
                    mafia.setTarget(attacker); // All nearby Mafia aggro on your killer
                }
            }
        }
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        ItemStack itemStack = new ItemStack(Items.ARROW);
        PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(this, itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.33) - arrow.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        arrow.setVelocity(d, e + g * 0.2, f, 1.6F, 1.0F);
        this.getWorld().spawnEntity(arrow);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> {
            if (this.type == MafiaType.ARCHER && this.isAttacking()) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.mafia.raise_bow"));
            }
            if (this.handSwinging) {
                String anim = (this.type == MafiaType.AXEMAN) ? "animation.mafia.attack_axe" : "animation.mafia.attack_sword";
                return state.setAndContinue(RawAnimation.begin().thenPlay(anim));
            }
            if (this.hurtTime > 0 && this.type == MafiaType.SWORDSMAN) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.mafia.raise_shield"));
            }
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.mafia.run"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.mafia.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("MafiaType", this.type.name());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("MafiaType")) {
            this.type = MafiaType.valueOf(nbt.getString("MafiaType"));
        }
    }
}
