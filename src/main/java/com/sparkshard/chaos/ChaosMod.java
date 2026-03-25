package com.sparkshard.chaos;

import com.sparkshard.chaos.commands.DisasterCommand;
import com.sparkshard.chaos.entity.EntityRegistry;
import com.sparkshard.chaos.world.MapGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class ChaosMod implements ModInitializer {
    public static final String MODID = "chaos";

    @Override
    public void onInitialize() {
        // 1. Register FBI/Mafia Entities
        EntityRegistry.register();

        // 2. Register /disaster fbi Command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DisasterCommand.register(dispatcher);
        });

        // 3. Map Generation (Triggered on First Join)
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerWorld world = handler.player.getServerWorld();
            
            // We check a specific block at the center (0, 64, 0). 
            // If it's AIR, we know the map hasn't been generated yet.
            BlockPos checkPos = new BlockPos(0, 64, 0);
            
            if (world.getBlockState(checkPos).isAir()) {
                System.out.println("--- CHAOS: GENERATING 500x500 MEGA CITY ---");
                
                // Disable mob spawning temporarily so the city builds cleanly
                world.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(false, server);
                
                // Generate the Terrain, Skyscrappers, Houses, and Bunkers
                MapGenerator.generateMegaMap(world, checkPos);
                
                // Teleport player to the safe center of the new city
                handler.player.teleport(0.5, 66, 0.5);
                
                System.out.println("--- CHAOS: CITY GENERATION COMPLETE ---");
            }
        });

        System.out.println("--- SPARKSHARD CHAOS MOD: INITIALIZED (FABRIC) ---");
    }
}
