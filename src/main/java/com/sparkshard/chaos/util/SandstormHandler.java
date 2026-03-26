package com.sparkshard.chaos.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import weather2.weathersystem.storm.StormObject;
import weather2.ServerTickHandler;

public class SandstormHandler {
    public static void spawnSandstorm(ServerWorld world, BlockPos pos, int intensity) {
        var manager = ServerTickHandler.getWeatherSystemForDimension(world);
        if (manager != null) {
            StormObject sand = new StormObject(manager);
            sand.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            sand.stormType = 3; // Custom ID for Sand
            sand.levelCurIntensityStage = intensity;
            sand.isSandstorm = true;
            sand.maxRadius = 400;

            // Logic to pick up dropped items (Guns, Loot) and throw them
            world.getEntitiesByClass(ItemEntity.class, sand.getBoundingBox().expand(50), item -> true)
                .forEach(item -> {
                    item.addVelocity(world.random.nextGaussian() * 0.5, 0.3, world.random.nextGaussian() * 0.5);
                });

            manager.addStormObject(sand);
            manager.syncStormUpdate(sand);
        }
    }
}
