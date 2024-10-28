package dev.ipoleksenko.pockethome.world;

import com.google.common.collect.ImmutableList;
import dev.ipoleksenko.pockethome.mixin.MinecraftServerAccess;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.storage.DerivedLevelData;
import org.jetbrains.annotations.Nullable;

public class RuntimeWorld extends ServerLevel {
    private static final ChunkProgressListener LISTENER = new ChunkProgressListener() {
        @Override
        public void updateSpawnPos(ChunkPos pCenter) {

        }

        @Override
        public void onStatusChange(ChunkPos pChunkPos, @Nullable ChunkStatus pChunkStatus) {

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }
    };

    public RuntimeWorld(MinecraftServer server, ResourceKey<Level> worldRegistryKey, long seed) {
        super(
                server,
                Util.backgroundExecutor(),
                ((MinecraftServerAccess) server).getStorageSource(),
                new DerivedLevelData(server.getWorldData(), server.getWorldData().overworldData()),
                worldRegistryKey,
                new LevelStem(
                        server.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD),
                        new FlatLevelSource(
                                server.registryAccess().lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET).getOrThrow(FlatLevelGeneratorPresets.THE_VOID).value().settings()
                        )
                ),
                LISTENER,
                false,
                seed,
                ImmutableList.of(),
                false,
                null
        );
    }
}
