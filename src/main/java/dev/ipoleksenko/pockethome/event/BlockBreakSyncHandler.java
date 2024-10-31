package dev.ipoleksenko.pockethome.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


public class BlockBreakSyncHandler {

    @SubscribeEvent
    public void onBlockBreak(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide()) {
            ServerLevel serverWorld = (ServerLevel) event.getLevel();
            BlockPos pos = event.getPos();
            ServerPlayer player = (ServerPlayer) event.getEntity();

            serverWorld.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            serverWorld.getChunkSource().chunkMap
                    .getPlayers(serverWorld.getChunkAt(pos).getPos(), false)
                    .forEach(p -> p.connection.send(new ClientboundBlockUpdatePacket(pos, Blocks.AIR.defaultBlockState())));
        }
    }
}
