package dev.ipoleksenko.pockethome.event;

import dev.ipoleksenko.pockethome.PocketHome;
import dev.ipoleksenko.pockethome.world.PlatformGenerator;
import dev.ipoleksenko.pockethome.world.RuntimeWorldManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import static dev.ipoleksenko.pockethome.PocketHome.MODID;

import java.util.HashSet;
import java.util.Set;

public class ChestInteractEvent {
    private RuntimeWorldManager runtimeWorldManager;
    private Set<String> playersWhoEnteredWorld = new HashSet<>();

    @SubscribeEvent
    public void onPlayerUseBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockState blockState = event.getLevel().getBlockState(event.getPos());

        // Check that the player is interacting with an ender chest and is on the server
        if (event.getLevel().isClientSide && !(player instanceof ServerPlayer)) return;
        if (!(blockState.is(Blocks.ENDER_CHEST))) return;
        if (!player.isCrouching()) return;

        // Get or create a custom world for the player
        var customWorld = runtimeWorldManager.getOrCreate(ResourceLocation.fromNamespaceAndPath(MODID, player.getStringUUID()));

        if (player instanceof ServerPlayer serverPlayer) {
            // Check if the player is in the custom world
            if (serverPlayer.level().dimension().equals(customWorld.dimension())) {
                // If the player is already in the custom world, teleport them back to the overworld
                serverPlayer.teleportTo(serverPlayer.getServer().getLevel(Level.OVERWORLD), 8.0, 100.0, 8.0, RelativeMovement.ROTATION, 0.0f, 0.0f);
            } else if (serverPlayer.level().dimension().equals(Level.OVERWORLD)) {
                PlatformGenerator.generateBedrockPlatform(customWorld, new BlockPos(-32, 0, -32));
                // If the player is entering the world for the first time, generate the platform
                if (playersWhoEnteredWorld.add(player.getStringUUID())) {
                    PlatformGenerator.generatePlatform(customWorld, new BlockPos(-32, 0, -32));
                }
                // Teleport the player to the custom world
                serverPlayer.teleportTo(customWorld, 8.0, 10.0, 8.0, RelativeMovement.ROTATION, 0.0f, 0.0f);
            }
        }

        // Set the event result to prevent other interactions
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        this.runtimeWorldManager = new RuntimeWorldManager(event.getServer());
    }
}
