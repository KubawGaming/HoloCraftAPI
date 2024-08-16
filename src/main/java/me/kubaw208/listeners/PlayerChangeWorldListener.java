package me.kubaw208.listeners;

import me.kubaw208.structs.Hologram;
import me.kubaw208.api.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorldListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        for(Hologram hologram : hologramManager.getHolograms()) {
            if(hologram.canSee(player) && hologram.getLocation().getWorld().equals(player.getWorld())) {
                hologram.showHologram(player, true);
            }
        }
    }

}
