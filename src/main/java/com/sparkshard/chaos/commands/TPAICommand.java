package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.entity.Entity;

public class TPAICommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpai")
            .requires(source -> source.hasPermission(2))
            .then(CommandManager.argument("name", StringArgumentType.string())
                .executes(context -> {
                    String targetName = StringArgumentType.getString(context, "name");
                    var player = context.getSource().getPlayer();
                    
                    for (Entity entity : context.getSource().getWorld().iterateEntities()) {
                        if (entity.hasCustomName() && entity.getCustomName().getString().equalsIgnoreCase(targetName)) {
                            player.teleport(entity.getX(), entity.getY(), entity.getZ(), false);
                            return 1;
                        }
                    }
                    return 0;
                })
            )
        );
    }
}
