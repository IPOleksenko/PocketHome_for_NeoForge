package dev.ipoleksenko.pockethome.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PlatformGenerator {
    private static final int CHUNK_SIZE = 16;
    private static final int PLATFORM_SIZE = CHUNK_SIZE * 4; // 4x4 chunks = 64x64 blocks
    private static final int BEDROCK_LAYER = -60;
    private static final int DIRT_LAYERS = 8;

    public static void generatePlatform(ServerLevel world, BlockPos origin) {
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();

        for (int x = 0; x < PLATFORM_SIZE; x++) {
            for (int z = 0; z < PLATFORM_SIZE; z++) {
                world.setBlock(origin.offset(x, BEDROCK_LAYER, z), bedrock, 3);

                for (int y = 1; y <= DIRT_LAYERS; y++) {
                    world.setBlock(origin.offset(x, BEDROCK_LAYER + y, z), dirt, 3);
                }

                world.setBlock(origin.offset(x, BEDROCK_LAYER + DIRT_LAYERS + 1, z), grass, 3);
            }
        }
    }

    public static void generateBedrockPlatform(ServerLevel world, BlockPos origin) {
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        for (int x = 0; x < PLATFORM_SIZE; x++) {
            for (int z = 0; z < PLATFORM_SIZE; z++) {
                world.setBlock(origin.offset(x, BEDROCK_LAYER, z), bedrock, 3);
            }
        }
    }
}
