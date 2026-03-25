package com.sparkshard.chaos;

import com.sparkshard.chaos.commands.DisasterCommand;
import com.sparkshard.chaos.entity.EntityRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ChaosMod implements ModInitializer {
    public static final String MODID = "chaos";

    @Override
    public void onInitialize() {
        // Register Entities
        EntityRegistry.register();

        // Register Commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DisasterCommand.register(dispatcher);
        });

        System.out.println("--- SPARKSHARD CHAOS MOD: INITIALIZED (FABRIC) ---");
    }
}
