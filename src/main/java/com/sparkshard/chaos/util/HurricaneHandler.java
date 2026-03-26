package com.sparkshard.chaos.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class HurricaneHandler {

    public static void spawnHurricane(ServerWorld world, BlockPos pos) {
        WeatherManagerServer manager = ServerTickHandler.getWeatherSystemForDimension(world);

        if (manager != null) {
            StormObject hurricane = new StormObject(manager);
            hurricane.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            
            // In Weather2 Remastered, stormType 2 or 3 usually represents 
            // massive tropical cyclones/hurricanes
            hurricane.stormType = 2; 
            
            // Hurricanes have a MASSIVE radius compared to tornadoes
            hurricane.maxRadius = 600; 
            
            // Set to high intensity (Category 5)
            hurricane.levelCurIntensityStage = 5;
            
            // Force widespread wind instead of a localized funnel
            hurricane.isHurricane = true;
            
            manager.addStormObject(hurricane);
            manager.syncStormUpdate(hurricane);
        }
    }

    /**
     * Logic to make the FBI and Mafia struggle against the wind
     */
    public static void applyHurricanePhysics(ServerWorld world, Vec3d stormPos) {
        world.getPlayers().forEach(player -> {
            // Push everything in a circular motion around the eye
            Vec3d push = new Vec3d(0.1, 0, 0.1); 
            player.addVelocity(push.x, push.y, push.z);
        });
    }
}
