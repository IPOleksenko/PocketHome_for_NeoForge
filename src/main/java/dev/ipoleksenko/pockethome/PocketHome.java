package dev.ipoleksenko.pockethome;

import dev.ipoleksenko.pockethome.event.BlockBreakSyncHandler;
import dev.ipoleksenko.pockethome.event.ChestInteractEvent;
import dev.ipoleksenko.pockethome.event.PlayerJoinEvent;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PocketHome.MODID)
public class PocketHome {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pockethome";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public PocketHome(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new ChestInteractEvent());
        NeoForge.EVENT_BUS.register(new PlayerJoinEvent());
        NeoForge.EVENT_BUS.register(new BlockBreakSyncHandler());
    }
}
