package com.sparkshard.chaos.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import com.sparkshard.chaos.entity.EntityFBI;
import com.sparkshard.chaos.entity.EntityMafia;

// Note: This requires the Weather2/CoroUtil classes to be in your build path
import weather2.api.WindReader; 

public class Weather2Integration {

    public static void applyTornadoPhysics(LivingEntity entity) {
        if (entity instanceof EntityFBI || entity instanceof EntityMafia) {
            // Get wind speed from Weather2 at the mob's position
            float windSpeed = WindReader.getWindSpeed(entity.getWorld(), entity.getPos());
            
            // If wind is high (Tornado level), override mob gravity
            if (windSpeed > 0.7f) {
                Vec3d windDir = WindReader.getWindAngle(entity.getWorld(), entity.getPos());
                
                // Lift them up and swirl them
                entity.addVelocity(
                    windDir.x * 0.2, 
                    0.15, 
                    windDir.z * 0.2
                );
                entity.velocityModified = true;
            }
        }
    }
}
