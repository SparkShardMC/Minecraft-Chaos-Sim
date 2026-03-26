package com.sparkshard.chaos.util;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class FirenadoHandler {

    public static void spawnFirenado(ServerWorld world, BlockPos pos, int intensity) {
        WeatherManagerServer manager = ServerTickHandler.getWeatherSystemForDimension(world);

        if (manager != null) {
            StormObject tornado = new StormObject(manager);
            tornado.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            
            tornado.stormType = 1; // Base Tornado
            tornado.levelCurIntensityStage = intensity;
            tornado.isDirectCreation = true;

            // --- THE TRANSFORMATION LOGIC ---
            // We force the "IsOnFire" state in the weather engine
            tornado.isFirenado = true; 

            // We also physically place fire at the feet of the tornado to ensure 
            // the particles stay orange and black (smoke)
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos firePos = pos.add(x, 0, z);
                    if (world.isAir(firePos)) {
                        world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
                    }
                }
            }

            manager.addStormObject(tornado);
            manager.syncStormUpdate(tornado);
        }
    }
}
