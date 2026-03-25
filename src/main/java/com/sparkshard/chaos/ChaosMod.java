package com.sparkshard.chaos;

import com.sparkshard.chaos.commands.DisasterCommand;
import com.sparkshard.chaos.commands.TPAICommand;
import com.sparkshard.chaos.entity.EntityRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(ChaosMod.MODID)
public class ChaosMod {
    public static final String MODID = "chaos";

    public ChaosMod(IEventBus modEventBus) {
        // Registers the FBI/Mafia entities to the game
        EntityRegistry.ENTITIES.register(modEventBus);

        // Registers this class to the NeoForge event bus so it listens for commands
        NeoForge.EVENT_BUS.register(this);
        
        System.out.println("--- SPARKSHARD CHAOS MOD: INITIALIZED ---");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // Links your command files to the game's / terminal
        DisasterCommand.register(event.getDispatcher());
        TPAICommand.register(event.getDispatcher());
        
        System.out.println("--- CHAOS COMMANDS: LOADED ---");
    }
}
