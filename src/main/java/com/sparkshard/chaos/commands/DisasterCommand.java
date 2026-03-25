package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sparkshard.chaos.entity.EntityFBI;
import com.sparkshard.chaos.entity.EntityRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class DisasterCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("disaster")
            .requires(source -> source.hasPermission(2)) // Level 2 = OP/Cheats enabled
            .then(Commands.argument("type", StringArgumentType.word())
                .suggests((context, builder) -> {
                    builder.suggest("fbi");
                    builder.suggest("tornado");
                    builder.suggest("mafia");
                    return builder.buildFuture();
                })
                .executes(context -> {
                    String type = StringArgumentType.getString(context, "type");
                    ServerLevel level = context.getSource().getLevel();
                    Vec3 pos = context.getSource().getPosition();

                    switch (type.toLowerCase()) {
                        case "fbi":
                            context.getSource().sendSuccess(() -> Component.literal("§b§l[!] DISPATCHING FBI BREACH SQUAD"), true);
                            for (int i = 0; i < 50; i++) {
                                EntityFBI fbi = new EntityFBI(EntityRegistry.FBI_AGENT.get(), level);
                                double angle = i * (2 * Math.PI / 50);
                                fbi.setPos(pos.x + Math.cos(angle) * 15, pos.y, pos.z + Math.sin(angle) * 15);
                                level.addFreshEntity(fbi);
                            }
                            break;

                        case "tornado":
                            context.getSource().sendSuccess(() -> Component.literal("§7§l[!] TORNADO SIRENS WAILING..."), true);
                            // Tornado spawning logic will go here
                            break;

                        default:
                            context.getSource().sendFailure(Component.literal("§cUnknown disaster: " + type));
                            return 0;
                    }
                    return 1;
                })
            )
        );
    }
}
