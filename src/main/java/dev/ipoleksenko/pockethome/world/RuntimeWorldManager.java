package dev.ipoleksenko.pockethome.world;

import dev.ipoleksenko.pockethome.mixin.MinecraftServerAccess;
import dev.ipoleksenko.pockethome.world.RuntimeWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


public class RuntimeWorldManager {
    private final MinecraftServer server;

    public RuntimeWorldManager(@NotNull MinecraftServer server) {
        this.server = server;
    }

    public ServerLevel getOrCreate(@NotNull ResourceLocation id) {
        final ResourceKey<Level> worldRegistryKey = ResourceKey.create(Registries.DIMENSION, id);
        final ServerLevel world = this.server.getLevel(worldRegistryKey);

        if (world != null) {
            return world;
        }

        final RuntimeWorld runtimeWorld = new RuntimeWorld(server, worldRegistryKey, 0);
        ((MinecraftServerAccess) this.server).getLevels().put(worldRegistryKey, runtimeWorld);

        return runtimeWorld;
    }
}
