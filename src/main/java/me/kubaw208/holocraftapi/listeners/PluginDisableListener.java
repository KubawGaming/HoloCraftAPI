package me.kubaw208.holocraftapi.listeners;

import me.kubaw208.holocraftapi.api.HologramManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginDisableListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        hologramManager.unregisterPacketListeners();
    }

}