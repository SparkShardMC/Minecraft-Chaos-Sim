package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class TPAICommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpai")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("name", StringArgumentType.string())
                .executes(context -> {
                    String targetName = StringArgumentType.getString(context, "name");
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    
                    for (Entity entity : context.getSource().getLevel().getAllEntities()) {
                        if (entity.hasCustomName() && 
                            entity.getCustomName().getString().equalsIgnoreCase(targetName)) {
                            
                            player.teleportTo(entity.getX(), entity.getY(), entity.getZ());
                            context.getSource().sendSuccess(() -> 
                                Component.literal("§aTeleported to §f" + targetName), true);
                            return 1;
                        }
                    }

                    context.getSource().sendFailure(Component.literal("§cEntity '" + targetName + "' not found!"));
                    return 0;
                })
            )
        );
    }
}
