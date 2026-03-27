package com.sparkshard.chaos.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class DeathLogic {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            String name = entity.getName().getString();
            var server = entity.getServer();
            if (server == null) return;

            // 1. The Death Announcement
            String cause = damageSource.getDeathMessage(entity).getString();
            server.getPlayerList().broadcast(Text.literal("§0[§4DEATH§0] §f" + name + " was eliminated: " + cause), false);

            // 2. The "Airplane Fart" Special Logic
            if (name.toLowerCase().contains("airplane fart")) {
                entity.getWorld().createExplosion(null, entity.getX(), entity.getY(), entity.getZ(), 2.0f, World.ExplosionSourceType.NONE);
                server.getPlayerList().broadcast(Text.literal("§eThe smell was too much..."), false);
            }

            // 3. The "Bob" Moment
            if (name.equalsIgnoreCase("Bob")) {
                server.getPlayerList().broadcast(Text.literal("§4§lTHE LEGEND HAS FALLEN. REBOOTING WORLD... (Just kidding)"), false);
            }
        });
    }
}
