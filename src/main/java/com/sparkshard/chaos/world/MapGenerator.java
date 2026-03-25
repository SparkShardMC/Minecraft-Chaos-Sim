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

                // 1. BASIC TERRAIN
                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
                world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
                world.setBlockState(pos.down(2), Blocks.STONE.getDefaultState());
                world.setBlockState(pos.down(3), Blocks.BEDROCK.getDefaultState());

                // 2. THE GRID (Roads every 25 blocks)
                if (x % 25 == 0 || z % 25 == 0) {
                    world.setBlockState(pos, Blocks.GRAY_CONCRETE.getDefaultState());
                    // Add yellow lines in the middle of roads
                    if ((x % 25 == 0 && z % 2 == 0) || (z % 25 == 0 && x % 2 == 0)) {
                        world.setBlockState(pos, Blocks.YELLOW_CONCRETE.getDefaultState());
                    }
                    continue; // Don't spawn buildings on the road!
                }

                // 3. SCATTERED GENERATION
                if (RANDOM.nextInt(1000) < 3) { // Slightly higher skyscraper chance
                    generateSkyscraper(world, pos.up());
                } else if (RANDOM.nextInt(1000) < 5) {
                    generateHouse(world, pos.up());
                } else if (RANDOM.nextInt(1000) < 2) { // More bunkers for FBI
                    generateBunker(world, pos.down(10));
                } else if (RANDOM.nextInt(500) < 1) {
                    world.setBlockState(pos.up(), Blocks.OAK_SAPLING.getDefaultState());
                }
            }
        }
        
        // 4. THE LAKE (Now spawns 3 lakes instead of 1 for a 500x500 area)
        for(int i = 0; i < 3; i++) {
            generateLake(world, center.add(RANDOM.nextInt(400)-200, 0, RANDOM.nextInt(400)-200));
        }
    }

    private static void generateSkyscraper(ServerWorld world, BlockPos pos) {
        int height = 30 + RANDOM.nextInt(50); // Taller buildings
        int size = 7 + RANDOM.nextInt(6);
        var glassType = RANDOM.nextBoolean() ? Blocks.GLASS.getDefaultState() : Blocks.CYAN_STAINED_GLASS.getDefaultState();
        var frameType = RANDOM.nextBoolean() ? Blocks.IRON_BLOCK.getDefaultState() : Blocks.SMOOTH_STONE.getDefaultState();

        for (int h = 0; h < height; h++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    BlockPos p = pos.add(x, h, z);
                    
                    // Corner Pillars (Frame)
                    if ((x == 0 || x == size - 1) && (z == 0 || z == size - 1)) {
                        world.setBlockState(p, frameType);
                    } 
                    // Walls (Glass)
                    else if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
                        // Use Glass Panes for the top 80% of the building (better for Tornadoes)
                        world.setBlockState(p, h > 2 ? Blocks.GLASS_PANE.getDefaultState() : glassType);
                    } 
                    // Floors
                    else if (h % 5 == 0) { 
                        world.setBlockState(p, Blocks.SMOOTH_STONE.getDefaultState());
                    }
                }
            }
        }
    }

    private static void generateHouse(ServerWorld world, BlockPos pos) {
        // Upgraded House with a Roof
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                for (int y = 0; y < 4; y++) {
                    // Hollow out the inside
                    if (x == 0 || x == 4 || z == 0 || z == 4 || y == 3) {
                        world.setBlockState(pos.add(x, y, z), Blocks.COBBLESTONE.getDefaultState());
                    } else {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
                // Add a simple roof
                world.setBlockState(pos.add(x, 4, z), Blocks.OAK_PLANKS.getDefaultState());
            }
        }
    }

    private static void generateBunker(ServerWorld world, BlockPos pos) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = 0; z < 10; z++) {
                    if (x == 0 || x == 9 || y == 0 || y == 5 || z == 0 || z == 9) {
                        world.setBlockState(pos.add(x, y, z), Blocks.OBSIDIAN.getDefaultState());
                    } else {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        // Add a ladder leading up to the surface
        for (int y = 0; y < 11; y++) {
            world.setBlockState(pos.add(5, y, 5), Blocks.LADDER.getDefaultState());
        }
    }

    private static void generateLake(ServerWorld world, BlockPos pos) {
        int lSize = 15 + RANDOM.nextInt(10);
        for (int x = 0; x < lSize; x++) {
            for (int z = 0; z < lSize; z++) {
                for (int y = 0; y > -5; y--) {
                    // Rounded lake check
                    if (Math.sqrt(Math.pow(x - lSize/2.0, 2) + Math.pow(z - lSize/2.0, 2)) < lSize/2.0) {
                        world.setBlockState(pos.add(x, y, z), Blocks.WATER.getDefaultState());
                    }
                }
            }
        }
    }
}
