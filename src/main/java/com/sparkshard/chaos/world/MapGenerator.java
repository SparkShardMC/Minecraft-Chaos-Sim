package com.sparkshard.chaos.world;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import java.util.Random;

public class MapGenerator {
    private static final Random RANDOM = new Random();

    public static void generateMegaMap(ServerWorld world, BlockPos center) {
        int radius = 250; 

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos pos = center.add(x, 0, z);

                // 1. BASIC TERRAIN (Grass/Dirt/Stone)
                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
                world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
                world.setBlockState(pos.down(2), Blocks.STONE.getDefaultState());
                world.setBlockState(pos.down(3), Blocks.BEDROCK.getDefaultState());

                // 2. SCATTERED GENERATION (Only happens on certain blocks)
                if (RANDOM.nextInt(1000) < 2) { // 0.2% chance for a skyscraper
                    generateSkyscraper(world, pos.up());
                } else if (RANDOM.nextInt(1000) < 5) { // 0.5% chance for a house
                    generateHouse(world, pos.up());
                } else if (RANDOM.nextInt(1000) < 1) { // 0.1% chance for a bunker
                    generateBunker(world, pos.down(10));
                } else if (RANDOM.nextInt(500) < 1) { // Trees
                    world.setBlockState(pos.up(), Blocks.OAK_SAPLING.getDefaultState());
                }
            }
        }
        
        // 3. THE LAKE (One single lake at a random spot)
        generateLake(world, center.add(RANDOM.nextInt(100)-50, 0, RANDOM.nextInt(100)-50));
    }

    private static void generateSkyscraper(ServerWorld world, BlockPos pos) {
        int height = 20 + RANDOM.nextInt(40);
        int size = 5 + RANDOM.nextInt(5);
        for (int h = 0; h < height; h++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    BlockPos p = pos.add(x, h, z);
                    // Walls are Glass or Cyan Stained Glass
                    if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
                        world.setBlockState(p, Blocks.GLASS.getDefaultState());
                    } else if (h % 5 == 0) { 
                        // Floors every 5 blocks
                        world.setBlockState(p, Blocks.SMOOTH_STONE.getDefaultState());
                    } else if (x == 2 && z == 2) {
                        // Simple Ladder/Stairs "Column"
                        world.setBlockState(p, Blocks.LADDER.getDefaultState());
                    }
                }
            }
        }
    }

    private static void generateHouse(ServerWorld world, BlockPos pos) {
        // Simple 5x5 Cobblestone House (Villager style)
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                for (int y = 0; y < 4; y++) {
                    world.setBlockState(pos.add(x, y, z), Blocks.COBBLESTONE.getDefaultState());
                }
                // Basement
                for (int y = -1; y > -5; y--) {
                    world.setBlockState(pos.add(x, y, z), Blocks.BRICKS.getDefaultState());
                }
            }
        }
    }

    private static void generateBunker(ServerWorld world, BlockPos pos) {
        // 10x10 Reinforced Bunker Deep Underground
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 10; z++) {
                    // Obsidian walls for strength
                    if (x == 0 || x == 9 || y == 0 || y == 4 || z == 0 || z == 9) {
                        world.setBlockState(pos.add(x, y, z), Blocks.OBSIDIAN.getDefaultState());
                    } else {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    private static void generateLake(ServerWorld world, BlockPos pos) {
        for (int x = 0; x < 20; x++) {
            for (int z = 0; z < 20; z++) {
                for (int y = 0; y > -5; y--) {
                    world.setBlockState(pos.add(x, y, z), Blocks.WATER.getDefaultState());
                }
            }
        }
    }
}
