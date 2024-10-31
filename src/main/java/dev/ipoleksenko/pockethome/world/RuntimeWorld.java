package dev.ipoleksenko.pockethome.world;

import com.google.common.collect.ImmutableList;
import dev.ipoleksenko.pockethome.mixin.MinecraftServerAccess;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class RuntimeWorld extends ServerLevel {
    private static final ChunkProgressListener LISTENER = new ChunkProgressListener() {
        @Override
        public void updateSpawnPos(ChunkPos pCenter) {}

        @Override
        public void onStatusChange(ChunkPos pChunkPos, @Nullable ChunkStatus pChunkStatus) {}

        @Override
        public void start() {}

        @Override
        public void stop() {}
    };

    public RuntimeWorld(MinecraftServer server, ResourceKey<Level> worldRegistryKey, long seed) {
        super(
                server,
                Util.backgroundExecutor(),
                ((MinecraftServerAccess) server).getStorageSource(),
                new DerivedLevelData(server.getWorldData(), server.getWorldData().overworldData()),
                worldRegistryKey,
                createLevelStem(server, seed),
                LISTENER,
                false,
                seed,
                ImmutableList.of(),
                false,
                null
        );
    }

    private static LevelStem createLevelStem(MinecraftServer server, long seed) {
        // Retrieve biome and structure as Holders
        Holder<net.minecraft.world.level.biome.Biome> biomeHolder = server.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getHolderOrThrow(Biomes.THE_VOID);

        HolderSet<net.minecraft.world.level.levelgen.structure.StructureSet> structureSetHolder =
                HolderSet.direct(List.of());

        // Configure custom layers (bedrock, dirt, grass)
        FlatLevelGeneratorSettings generatorSettings = new FlatLevelGeneratorSettings(
                Optional.of(structureSetHolder),
                biomeHolder,
                List.of()
        );

        // Create a generator with our custom FlatLevelGeneratorSettings
        ChunkGenerator chunkGenerator = new FlatLevelSource(generatorSettings);

        return new LevelStem(
                server.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD),
                chunkGenerator
        );
    }
}
