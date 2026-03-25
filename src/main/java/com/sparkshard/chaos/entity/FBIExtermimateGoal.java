package com.sparkshard.chaos.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import java.util.EnumSet;

public class FBIExterminateGoal extends Goal {
    private final EntityFBI fbi;
    private PlayerEntity target;
    private int animationTick = 0;

    public FBIExterminateGoal(EntityFBI fbi) {
        this.fbi = fbi;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        this.target = this.fbi.getWorld().getClosestPlayer(this.fbi, 500); // 500 block track range
        return this.target != null;
    }

    @Override
    public void tick() {
        this.fbi.getLookControl().lookAt(this.target, 30.0F, 30.0F);
        double distance = this.fbi.squaredDistanceTo(this.target);

        // 1. PATHFINDING TO PLAYER
        this.fbi.getNavigation().startMovingTo(this.target, 1.5D);

        // 2. DOOR KICKING LOGIC
        BlockPos headPos = this.fbi.getBlockPos().up();
        if (this.fbi.getWorld().getBlockState(headPos).getBlock().getName().getString().contains("Door")) {
            startKicking(headPos);
        }

        // 3. ATTACKING PLAYER
        if (distance < 4.0D) {
            startKicking(this.target.getBlockPos());
            if (animationTick == 10) { // Hit halfway through animation
                this.fbi.tryAttack(this.target);
            }
        }

        if (animationTick > 0) animationTick--;
    }

    private void startKicking(BlockPos pos) {
        if (animationTick <= 0) {
            this.fbi.setAttacking(true); // Triggers the animation controller
            animationTick = 20; // Length of animation
            
            // If it's a door, break it!
            if (this.fbi.getWorld().getBlockState(pos).getBlock().getName().getString().contains("Door")) {
                this.fbi.getWorld().breakBlock(pos, false);
            }
        }
    }
}
