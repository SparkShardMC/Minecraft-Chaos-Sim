package com.sparkshard.chaos.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
// These imports connect directly to the Weather Remastered Logic
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class WeatherController {

    public static void spawnCustomTornado(ServerWorld world, BlockPos pos, int intensity) {
        // 1. Get the Weather Manager for the current world
        WeatherManagerServer manager = ServerTickHandler.getWeatherSystemForDimension(world);

        if (manager != null) {
            // 2. Create the Storm Object (The actual Tornado logic)
            StormObject tornado = new StormObject(manager);
            
            // 3. Set the Location to where the player is
            tornado.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            
            // 4. Set the Type to Tornado (Type 1)
            tornado.stormType = 1;
            
            // 5. Set the Intensity based on your /disaster tornado <power> command
            tornado.levelCurIntensityStage = intensity;
            
            // 6. Force the storm to be "Active" so it doesn't just vanish
            tornado.isDirectCreation = true;
            
            // 7. Tell the Weather Mod to add this to the world
            manager.addStormObject(tornado);
            
            // 8. Sync with all players so they see the clouds and funnel
            manager.syncStormUpdate(tornado);
        }
    }
}
