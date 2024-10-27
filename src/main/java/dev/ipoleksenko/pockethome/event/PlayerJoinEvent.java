package dev.ipoleksenko.pockethome.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerJoinEvent {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Send a welcome message every time the player joins the server
            player.sendSystemMessage(Component.nullToEmpty(String.format("""
            Hello, %s
            I'm a PocketHome
            ---------------------------------------------
            To join your pocket home,
            use an ender chest while sneaking.
            """, player.getName().getString()
            )));
        }
    }
}
