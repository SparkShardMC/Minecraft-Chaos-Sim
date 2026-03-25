package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.sparkshard.chaos.entity.EntityFBI;
import com.sparkshard.chaos.entity.EntityRegistry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DisasterCommand {
    public static void register(CommandDispatcher<ServerCommandSource, ?> dispatcher) {
        dispatcher.register(CommandManager.literal("disaster")
            .then(CommandManager.literal("fbi")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerWorld world = source.getWorld();
                    BlockPos pos = BlockPos.ofFloored(source.getPosition());

                    for (int i = 0; i < 50; i++) {
                        EntityFBI fbi = new EntityFBI(EntityRegistry.FBI_AGENT, world);
                        double x = pos.getX() + (world.random.nextDouble() * 16 - 8);
                        double z = pos.getZ() + (world.random.nextDouble() * 16 - 8);
                        
                        fbi.refreshPositionAndAngles(x, pos.getY(), z, world.random.nextFloat() * 360, 0);
                        world.spawnEntity(fbi);
                    }

                    source.sendFeedback(() -> Text.literal("§l§c[!] FBI OPEN UP!"), true);
                    return 1;
                })
            )
        );
    }
}
