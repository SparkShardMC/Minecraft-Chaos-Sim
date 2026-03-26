package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sparkshard.chaos.entity.EntityFBI;
import com.sparkshard.chaos.entity.EntityMafia;
import com.sparkshard.chaos.entity.EntityRegistry;
import com.sparkshard.chaos.util.WeatherController;
import com.sparkshard.chaos.util.HurricaneHandler;
import com.sparkshard.chaos.world.MapGenerator;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import com.sparkshard.chaos.util.HailHandler;
import com.sparkshard.chaos.util.SandstormHandler;
import com.sparkshard.chaos.util.BlizzardHandler;
import com.sparkshard.chaos.util.FirenadoHandler;

public class DisasterCommand {
    public static void register(CommandDispatcher<ServerCommandSource, ?> dispatcher) {
        dispatcher.register(CommandManager.literal("disaster")
            // 1. Spawns
            .then(CommandManager.literal("fbi").executes(context -> spawnEntityGroup(context.getSource(), "fbi")))
            .then(CommandManager.literal("mafia").executes(context -> spawnEntityGroup(context.getSource(), "mafia")))
            .then(CommandManager.literal("war").executes(context -> spawnEntityGroup(context.getSource(), "war")))

            // 2. Tornado (f0-f5)
            .then(CommandManager.literal("tornado")
                .then(CommandManager.argument("power", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("f0").suggest("f1").suggest("f2").suggest("f3").suggest("f4").suggest("f5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String power = StringArgumentType.getString(context, "power");
                        ServerCommandSource source = context.getSource();
                        BlockPos pos = BlockPos.ofFloored(source.getPosition());
                        int intensity = parseIntensity(power);

                        WeatherController.spawnCustomTornado(source.getWorld(), pos, intensity);
                        source.sendFeedback(() -> Text.literal("§6§l[!] WEATHER ALERT: Tornado " + power.toUpperCase() + " touched down!"), true);
                        return 1;
                    })
                )
            )

            // 3. Hurricane (type1-type5)
            .then(CommandManager.literal("hurricane")
                .then(CommandManager.argument("type", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("type1").suggest("type2").suggest("type3").suggest("type4").suggest("type5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String typeStr = StringArgumentType.getString(context, "type");
                        ServerCommandSource source = context.getSource();
                        BlockPos pos = BlockPos.ofFloored(source.getPosition());
                        
                        // Extract number from "typeX"
                        int intensity = parseHurricaneType(typeStr);

                        HurricaneHandler.spawnHurricane(source.getWorld(), pos, intensity);
                        source.sendFeedback(() -> Text.literal("§1§l[!!!] HURRICANE WARNING: " + typeStr.toUpperCase() + " incoming!"), true);
                        return 1;
                    })
                )
            )

            // 4. City Builder
            .then(CommandManager.literal("build_city")
                .executes(context -> {
                    BlockPos pos = BlockPos.ofFloored(context.getSource().getPosition());
                    MapGenerator.generateMegaMap(context.getSource().getWorld(), pos);
                    context.getSource().sendFeedback(() -> Text.literal("§a§l[!] City Grid Generated! Prepare for Chaos."), true);
                    return 1;
                }))
        );
    }

    private static int parseIntensity(String power) {
        return switch (power.toLowerCase()) {
            case "f1" -> 1;
            case "f2" -> 2;
            case "f3" -> 3;
            case "f4" -> 4;
            case "f5" -> 5;
            default -> 0;
        };
    }

    private static int parseHurricaneType(String type) {
        return switch (type.toLowerCase()) {
            case "type2" -> 2;
            case "type3" -> 3;
            case "type4" -> 4;
            case "type5" -> 5;
            default -> 1; // Default to type1
        };
    }

    private static int spawnEntityGroup(ServerCommandSource source, String type) {
        ServerWorld world = source.getWorld();
        BlockPos pos = BlockPos.ofFloored(source.getPosition());
        
        if (type.equals("fbi") || type.equals("war")) {
            for (int i = 0; i < 50; i++) {
                EntityFBI fbi = new EntityFBI(EntityRegistry.FBI_AGENT, world);
                double x = pos.getX() + (world.random.nextDouble() * 24 - 12);
                double z = pos.getZ() + (world.random.nextDouble() * 24 - 12);
                fbi.refreshPositionAndAngles(x, pos.getY(), z, world.random.nextFloat() * 360, 0);
                world.spawnEntity(fbi);
            }
        }

        if (type.equals("mafia") || type.equals("war")) {
            for (int i = 0; i < 50; i++) {
                EntityMafia mafia = new EntityMafia(EntityRegistry.MAFIA_MEMBER, world);
                double x = pos.getX() + (world.random.nextDouble() * 24 - 12);
                double z = pos.getZ() + (world.random.nextDouble() * 24 - 12);
                mafia.refreshPositionAndAngles(x, pos.getY(), z, world.random.nextFloat() * 360, 0);
                world.spawnEntity(mafia);
            }
        }

        switch (type) {
            case "fbi" -> source.sendFeedback(() -> Text.literal("§l§c[!] FBI RAID IN PROGRESS"), true);
            case "mafia" -> source.sendFeedback(() -> Text.literal("§l§8[!] MAFIA TAKEOVER STARTED"), true);
            case "war" -> source.sendFeedback(() -> Text.literal("§4§l[!!!] TERRITORY WAR: FBI VS MAFIA"), true);
        }
        return 1;
    }
}
