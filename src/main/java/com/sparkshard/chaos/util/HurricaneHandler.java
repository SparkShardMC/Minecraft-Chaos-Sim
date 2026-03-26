package com.sparkshard.chaos.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class HurricaneHandler {

    public static void spawnHurricane(ServerWorld world, BlockPos pos, int intensity) {
        WeatherManagerServer manager = ServerTickHandler.getWeatherSystemForDimension(world);

        if (manager != null) {
            StormObject hurricane = new StormObject(manager);
            hurricane.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            
            // Keeps the internal ID as Hurricane (Type 2)
            hurricane.stormType = 2; 
            
            // This is where your "Type 1-5" logic actually lives!
            // It scales the stage of the storm from a tropical gale to a Cat 5.
            hurricane.levelCurIntensityStage = intensity;
            
            // Scale the size: Type 1 = 200 blocks, Type 5 = 700 blocks
            hurricane.maxRadius = 150 + (intensity * 110); 
            
            // Essential Hurricane Flags
            hurricane.isHurricane = true;
            hurricane.isDirectCreation = true;
            
            // Add and Sync
            manager.addStormObject(hurricane);
            manager.syncStormUpdate(hurricane);
        }
    }
}
