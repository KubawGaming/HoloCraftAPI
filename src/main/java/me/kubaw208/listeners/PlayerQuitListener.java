package me.kubaw208.listeners;

import me.kubaw208.structs.Hologram;
import me.kubaw208.api.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for(Hologram hologram : hologramManager.getHolograms()) {
            if(hologram.getPlayersSeeingHologram().contains(player)) {
                hologram.getPlayersSeeingHologram().remove(player);
            }
        }
    }

}