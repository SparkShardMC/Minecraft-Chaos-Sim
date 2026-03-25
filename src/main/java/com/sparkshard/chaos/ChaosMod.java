package com.sparkshard.chaos;

import com.sparkshard.chaos.commands.DisasterCommand;
import com.sparkshard.chaos.entity.EntityRegistry;
import com.sparkshard.chaos.sound.ModSounds;
import com.sparkshard.chaos.world.MapGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChaosMod implements ModInitializer {
    public static final String MODID = "chaos";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        // 1. Register FBI, Mafia, and Sound Events
        // This ensures GeckoLib and Minecraft recognize "mafia_member"
        EntityRegistry.register();
        ModSounds.register(); 

        // 2. Register Commands (/disaster fbi, /disaster mafia, /disaster war)
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DisasterCommand.register(dispatcher);
        });

        // 3. 500x500 City Generation (Triggers on first join)
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerWorld world = handler.player.getServerWorld();
            
            // Checking the center point (0, 64, 0)
            BlockPos checkPos = new BlockPos(0, 64, 0);
            
            if (world.getBlockState(checkPos).isAir()) {
                LOGGER.info("--- CHAOS: GENERATING 500x500 MEGA CITY ---");
                
                // Disable mob spawning so the city builds without distractions
                world.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(false, server);
                
                // 🏗️ Generate the Terrain, Skyscrapers, Houses, and Bunkers
                // The MapGenerator will now also place Mafia Spawners in house basements
                MapGenerator.generateMegaMap(world, checkPos);
                
                // Teleport player to the safe center (0, 66, 0)
                handler.player.teleport(0.5, 66.0, 0.5);
                
                // Re-enable spawning after the city is "Ready for War"
                world.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(true, server);
                
                LOGGER.info("--- CHAOS: CITY GENERATION COMPLETE ---");
            }
        });

        LOGGER.info("--- SPARKSHARD CHAOS MOD: INITIALIZED (FABRIC) ---");
    }
}
