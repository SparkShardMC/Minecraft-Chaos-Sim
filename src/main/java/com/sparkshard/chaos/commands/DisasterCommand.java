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

            // 2. Tornado
            .then(CommandManager.literal("tornado")
                .then(CommandManager.argument("power", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("f0").suggest("f1").suggest("f2").suggest("f3").suggest("f4").suggest("f5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String power = StringArgumentType.getString(context, "power");
                        int intensity = parseIntensity(power);
                        WeatherController.spawnCustomTornado(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), intensity);
                        context.getSource().sendFeedback(() -> Text.literal("§6§l[!] WEATHER ALERT: Tornado " + power.toUpperCase() + " touched down!"), true);
                        return 1;
                    })
                )
            )

            // 3. Hurricane
            .then(CommandManager.literal("hurricane")
                .then(CommandManager.argument("type", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("type1").suggest("type2").suggest("type3").suggest("type4").suggest("type5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String typeStr = StringArgumentType.getString(context, "type");
                        int intensity = parseHurricaneType(typeStr);
                        HurricaneHandler.spawnHurricane(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), intensity);
                        context.getSource().sendFeedback(() -> Text.literal("§1§l[!!!] HURRICANE WARNING: " + typeStr.toUpperCase() + " incoming!"), true);
                        return 1;
                    })
                )
            )

            // 4. Hail
            .then(CommandManager.literal("hail")
                .then(CommandManager.argument("size", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("size1").suggest("size2").suggest("size3").suggest("size4").suggest("size5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String sizeStr = StringArgumentType.getString(context, "size");
                        int intensity = Integer.parseInt(sizeStr.replace("size", ""));
                        HailHandler.spawnHailStorm(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), intensity);
                        context.getSource().sendFeedback(() -> Text.literal("§b§l[!] HAILSTORM WARNING"), true);
                        return 1;
                    })
                )
            )

            // 5. Sandstorm
            .then(CommandManager.literal("sandstorm")
                .executes(context -> {
                    SandstormHandler.spawnSandstorm(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), 5);
                    context.getSource().sendFeedback(() -> Text.literal("§e§l[!] SANDSTORM APPROACHING"), true);
                    return 1;
                }))

            // 6. Snowstorm
            .then(CommandManager.literal("snowstorm")
                .executes(context -> {
                    BlizzardHandler.spawnBlizzard(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), 5);
                    context.getSource().sendFeedback(() -> Text.literal("§f§l[!] BLIZZARD WARNING"), true);
                    return 1;
                }))

            // 7. Firenado
            .then(CommandManager.literal("firenado")
                .then(CommandManager.argument("power", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("f1").suggest("f2").suggest("f3").suggest("f4").suggest("f5");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String power = StringArgumentType.getString(context, "power");
                        int intensity = parseIntensity(power);
                        FirenadoHandler.spawnFirenado(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), intensity);
                        context.getSource().sendFeedback(() -> Text.literal("§6§l[!] EMERGENCY: §c§lFIRENADO SIGHTED"), true);
                        return 1;
                    })
                )
            )

            // 8. City Builder
            .then(CommandManager.literal("build_city")
                .executes(context -> {
                    MapGenerator.generateMegaMap(context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()));
                    context.getSource().sendFeedback(() -> Text.literal("§a§l[!] City Grid Generated!"), true);
                    return 1;
                }))
        );
    }
