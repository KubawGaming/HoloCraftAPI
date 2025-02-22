package me.kubaw208.holocraftapi.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.kubaw208.holocraftapi.api.HologramManager;
import me.kubaw208.holocraftapi.packets.RemoveEntitiesPacket;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.List;

public class PlayerChangeWorldListener implements Listener {

    private final HologramManager hologramManager = HologramManager.getInstance();
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        for(Hologram hologram : hologramManager.getHolograms()) {
            if(hologram.canSee(player) && hologram.getLocation().getWorld().equals(player.getWorld())) {
                hologram.showHologram(player, true);
            } else {
                protocolManager.sendServerPacket(player, new RemoveEntitiesPacket(List.of(hologram.getEntityID())));
            }
        }
    }

}
