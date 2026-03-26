package com.sparkshard.chaos.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import weather2.ServerTickHandler;
import weather2.weathersystem.storm.StormObject;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // Only run on server, every 10 ticks (0.5 seconds) to prevent lag
        if (!entity.getWorld().isClient && entity.age % 10 == 0) {
            ServerWorld world = (ServerWorld) entity.getWorld();
            
            // 1. TORNADO REACTION: If flying and not on ground
            if (entity.getVelocity().y > 0.2 && !entity.isOnGround()) {
                // Play scream (using villager NO as a placeholder if scream.ogg isn't ready)
                world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_VILLAGER_NO, 
                    SoundCategory.HOSTILE, 1.0f, 1.0f);
                
                // Set the "Flailing" pose
                entity.setPose(EntityPose.LONG_JUMPING); 
            }

            // 2. FIRENADO REACTION: Check for nearby fire storms
            var manager = ServerTickHandler.getWeatherSystemForDimension(world);
            if (manager != null) {
                for (StormObject storm : manager.getStormObjects()) {
                    if (storm.isFirenado && storm.pos.distanceTo(entity.getPos()) < 15) {
                        entity.setOnFireFor(8);
                    }
                }
            }
        }
    }
}
