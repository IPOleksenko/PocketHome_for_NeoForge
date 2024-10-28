package dev.ipoleksenko.pockethome.event;

import dev.ipoleksenko.pockethome.PocketHome;
import dev.ipoleksenko.pockethome.RuntimeWorldManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.Set;

public class ChestInteractEvent {
    private RuntimeWorldManager runtimeWorldManager;

    @SubscribeEvent
    public void onPlayerUseBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockState blockState = event.getLevel().getBlockState(event.getPos());

        // Check if the clicked block is a chest
        if (event.getLevel().isClientSide && !(player instanceof ServerPlayer)) return;
        if (!(blockState.is(Blocks.ENDER_CHEST))) return;
        if (!player.isCrouching()) return;


        var world = runtimeWorldManager.getOrCreate(ResourceLocation.fromNamespaceAndPath(PocketHome.MODID, player.getStringUUID()));
        player.teleportTo(world, 8.0, -60.0, 8.0, RelativeMovement.ROTATION, 0.0f, 0.0f);

        // Set the result to SUCCESS to prevent other interactions
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        this.runtimeWorldManager = new RuntimeWorldManager(event.getServer());
    }
}
