package com.sparkshard.chaos.util;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import weather2.weathersystem.storm.StormObject;
import weather2.ServerTickHandler;

public class BlizzardHandler {
    public static void spawnBlizzard(ServerWorld world, BlockPos pos, int intensity) {
        var manager = ServerTickHandler.getWeatherSystemForDimension(world);
        if (manager != null) {
            StormObject blizzard = new StormObject(manager);
            blizzard.pos = new net.minecraft.util.math.Vec3d(pos.getX(), pos.getY(), pos.getZ());
            blizzard.isBlizzard = true;
            blizzard.levelCurIntensityStage = intensity;
            blizzard.maxRadius = 500;

            // Freeze Logic: Turn nearby water to ice and ground to snow
            BlockPos.iterateOutwards(pos, 20, 5, 20).forEach(p -> {
                if (world.getBlockState(p).isOf(Blocks.WATER)) {
                    world.setBlockState(p, Blocks.ICE.getDefaultState());
                } else if (world.isAir(p) && world.getBlockState(p.down()).isOpaque()) {
                    world.setBlockState(p, Blocks.SNOW.getDefaultState());
                }
            });

            manager.addStormObject(blizzard);
            manager.syncStormUpdate(blizzard);
        }
    }
}
