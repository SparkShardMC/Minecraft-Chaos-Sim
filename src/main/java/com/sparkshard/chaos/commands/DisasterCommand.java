package com.sparkshard.chaos.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sparkshard.chaos.entity.EntityFBI;
import com.sparkshard.chaos.entity.EntityMafia;
import com.sparkshard.chaos.entity.EntityRegistry;
import com.sparkshard.chaos.util.WeatherController;
import com.sparkshard.chaos.world.MapGenerator;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DisasterCommand {
    public static void register(CommandDispatcher<ServerCommandSource, ?> dispatcher) {
        dispatcher.register(CommandManager.literal("disaster")
            // 1. /disaster fbi
            .then(CommandManager.literal("fbi")
                .executes(context -> spawnEntityGroup(context.getSource(), "fbi")))
            
            // 2. /disaster mafia
            .then(CommandManager.literal("mafia")
                .executes(context -> spawnEntityGroup(context.getSource(), "mafia")))
            
            // 3. /disaster war (FBI + Mafia only)
            .then(CommandManager.literal("war")
                .executes(context -> spawnEntityGroup(context.getSource(), "war")))

            // 4. /disaster tornado <power> (e.g., /disaster tornado f5)
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

                        // Map the string (f0-f5) to an integer (0-5)
                        int intensity = parseIntensity(power);

                        // Trigger the Weather and Tornadoes Remastered mod logic
                        WeatherController.spawnCustomTornado(source.getWorld(), pos, intensity);

                        source.sendFeedback(() -> Text.literal("§6§l[!] WEATHER ALERT: Tornado " + power.toUpperCase() + " touched down!"), true);
                        return 1;
                    })
                )
            )

            // 5. /disaster build_city (Triggers the MapGenerator)
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
            default -> 0; // f0 or invalid input
        };
    }

    private static int spawnEntityGroup(ServerCommandSource source, String type) {
        ServerWorld world = source.getWorld();
        BlockPos pos = BlockPos.ofFloored(source.getPosition());
        
        // Spawn FBI
        if (type.equals("fbi") || type.equals("war")) {
            for (int i = 0; i < 50; i++) {
                EntityFBI fbi = new EntityFBI(EntityRegistry.FBI_AGENT, world);
                double x = pos.getX() + (world.random.nextDouble() * 24 - 12);
                double z = pos.getZ() + (world.random.nextDouble() * 24 - 12);
                fbi.refreshPositionAndAngles(x, pos.getY(), z, world.random.nextFloat() * 360, 0);
                world.spawnEntity(fbi);
            }
        }

        // Spawn Mafia
        if (type.equals("mafia") || type.equals("war")) {
            for (int i = 0; i < 50; i++) {
                EntityMafia mafia = new EntityMafia(EntityRegistry.MAFIA_MEMBER, world);
                double x = pos.getX() + (world.random.nextDouble() * 24 - 12);
                double z = pos.getZ() + (world.random.nextDouble() * 24 - 12);
                mafia.refreshPositionAndAngles(x, pos.getY(), z, world.random.nextFloat() * 360, 0);
                world.spawnEntity(mafia);
            }
        }

        // Feedbacks
        switch (type) {
            case "fbi" -> source.sendFeedback(() -> Text.literal("§l§c[!] FBI RAID IN PROGRESS"), true);
            case "mafia" -> source.sendFeedback(() -> Text.literal("§l§8[!] MAFIA TAKEOVER STARTED"), true);
            case "war" -> source.sendFeedback(() -> Text.literal("§4§l[!!!] TERRITORY WAR: FBI VS MAFIA"), true);
        }

        return 1;
    }
}
