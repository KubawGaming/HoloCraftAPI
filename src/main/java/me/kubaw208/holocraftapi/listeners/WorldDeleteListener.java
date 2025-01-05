package me.kubaw208.holocraftapi.listeners;

import me.kubaw208.holocraftapi.api.HologramManager;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;

public class WorldDeleteListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent e) {
        for(Hologram hologram : new ArrayList<>(hologramManager.getHolograms())) {
            if(!hologram.getLocation().getWorld().equals(e.getWorld())) continue;

            hologramManager.deleteHologram(hologram);
        }
    }

}