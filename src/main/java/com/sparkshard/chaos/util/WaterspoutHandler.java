package com.sparkshard.chaos.util;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import weather2.weathersystem.storm.StormObject;

public class WaterspoutHandler {

    /**
     * This method is called by the storm's tick logic.
     * It checks if the tornado is over the lakes we built in MapGenerator.
     */
    public static void processWaterspoutLogic(StormObject storm, ServerWorld world) {
        BlockPos basePos = BlockPos.ofFloored(storm.pos.x, storm.pos.y, storm.pos.z);
        
        // 1. Check if the block beneath the tornado is Water
        if (world.getBlockState(basePos).getBlock() == Blocks.WATER || 
            world.getBlockState(basePos.down()).getBlock() == Blocks.WATER) {
            
            // 2. Tell the mod to use Water Particles (Type 2 in the engine)
            storm.isWaterspout = true;
            
            // 3. Apply "Sucking" force to nearby water-based entities
            applyWaterSuction(storm, world);
        } else {
            storm.isWaterspout = false;
        }
    }

    private static void applyWaterSuction(StormObject storm, ServerWorld world) {
        // Find entities within 30 blocks of the spout
        world.getOtherEntities(null, storm.getBoundingBox().expand(30)).forEach(entity -> {
            if (entity.isTouchingWater()) {
                // Pull them upward faster because water is "sticky" in our chaos logic
                entity.addVelocity(0, 0.2, 0);
            }
        });
    }
}
