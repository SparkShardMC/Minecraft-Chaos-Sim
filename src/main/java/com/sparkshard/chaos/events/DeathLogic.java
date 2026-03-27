package com.sparkshard.chaos.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class DeathLogic {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            var server = entity.getServer();
            if (server == null) return;
            
            String name = entity.getName().getString();
            server.getPlayerList().broadcast(Text.literal("§0[§4DEATH§0] §f" + name + " was eliminated."), false);

            if (name.toLowerCase().contains("airplane fart")) {
                entity.getWorld().createExplosion(null, entity.getX(), entity.getY(), entity.getZ(), 3.0f, World.ExplosionSourceType.TNT);
            }
        });
    }
}
