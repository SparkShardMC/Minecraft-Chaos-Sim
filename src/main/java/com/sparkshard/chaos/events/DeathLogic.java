package com.sparkshard.chaos.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class DeathLogic {

    @SubscribeEvent
    public void onAIDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player ai) {
            String name = ai.getName().getString();
            String cause = event.getSource().getLocalizedDeathMessage(ai).getString();

            // 1. The Death Announcement
            Component deathMsg = Component.literal("§0[§4DEATH§0] §f" + name + " was eliminated by " + cause);
            ai.level().getServer().getPlayerList().broadcastSystemMessage(deathMsg, false);

            // 2. The "Krakatoa" Special Logic
            if (name.contains("airplane fart")) {
                // If the guy with the long name dies, he goes out with a BANG
                ai.level().explode(null, ai.getX(), ai.getY(), ai.getZ(), 2.0f, false, Level.ExplosionInteraction.NONE);
                ai.level().getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal("§eThe smell was too much..."), false);
            }

            // 3. The "Bob" Moment
            if (name.equals("Bob")) {
                ai.level().getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal("§4§lTHE LEGEND HAS FALLEN. REBOOTING WORLD... (Just kidding)"), false);
            }
        }
    }
}
