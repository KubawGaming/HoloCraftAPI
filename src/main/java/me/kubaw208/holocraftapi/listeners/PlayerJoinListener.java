package me.kubaw208.holocraftapi.listeners;

import me.kubaw208.holocraftapi.structs.Hologram;
import me.kubaw208.holocraftapi.api.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        for(Hologram hologram : hologramManager.getHolograms()) {
            if(!hologram.isPublicVisible()) continue;

            hologram.showHologram(player);
        }
    }

}