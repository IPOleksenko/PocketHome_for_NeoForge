package dev.ipoleksenko.pockethome.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ChestInteractEvent {
    @SubscribeEvent
    public void onPlayerUseBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockState blockState = event.getLevel().getBlockState(event.getPos());

        // Check if the clicked block is a chest
        if (event.getLevel().isClientSide && !(player instanceof ServerPlayer)) return;
        if (!(blockState.is(Blocks.ENDER_CHEST))) return;
        if (!player.isCrouching()) return;

        player.sendSystemMessage(Component.nullToEmpty("Ender Chest"));

        // Set the result to SUCCESS to prevent other interactions
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
