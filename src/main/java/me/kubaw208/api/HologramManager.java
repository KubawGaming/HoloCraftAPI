package me.kubaw208.api;

import lombok.Getter;
import me.kubaw208.structs.Hologram;
import me.kubaw208.enums.HologramType;
import me.kubaw208.listeners.PlayerChangeWorldListener;
import me.kubaw208.listeners.PlayerJoinListener;
import me.kubaw208.listeners.PlayerQuitListener;
import me.kubaw208.listeners.custom.HologramCreateEvent;
import me.kubaw208.listeners.custom.HologramDeleteEvent;
import me.kubaw208.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HologramManager {

    @Getter private static Plugin plugin;
    @Getter private static HologramManager instance;
    /**
     * List where you can find instances of holograms
     * You won't find holograms that were removed after reloading the plugin
     * If you want those holograms to continue working properly you need to add them to this list again
     * @Warning Don't make changes to the list if you're not sure what you're doing!
     */
    @Getter private final ArrayList<Hologram> holograms = new ArrayList<>();
    private final AtomicInteger placeholdersUpdaterTaskID = new AtomicInteger();
    private int placeholdersUpdaterIntervalInTicks;

    public HologramManager(Plugin plugin) {
        HologramManager.plugin = plugin;
        instance = this;

        for(Hologram hologram : new ArrayList<>(holograms)) {
            if(hologram.isHideOnUnload())
                deleteHologram(hologram);
        }

        registerListeners(new PlayerJoinListener(), new PlayerQuitListener(), new PlayerChangeWorldListener());
        setPlaceholdersUpdaterTaskInterval(200); //Default refresh placeholders in all holograms every 10 seconds
    }

    /**
     * Creates new hologram depending on hologram type
     * @returns new hologram instance or null if custom event of creating hologram is canceled
     */
    public Hologram createHologram(HologramType hologramType, Location location) {
        HologramCreateEvent customEvent = new HologramCreateEvent(new Hologram(hologramType, location));

        customEvent.callEvent();

        if(customEvent.isCancelled()) return null;

        holograms.add(customEvent.getHologram());

        return customEvent.getHologram();
    }

    /**
     * Deletes hologram and automatically removes it from players visibility if custom event of deleting hologram is not canceled
     */
    public void deleteHologram(Hologram hologram) {
        HologramDeleteEvent customEvent = new HologramDeleteEvent(hologram);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return;

        for(Player player : new ArrayList<>(hologram.getPlayersSeeingHologram())) {
            hologram.hideHologram(player);
        }
        holograms.remove(hologram);
    }

    /**
     * Sets how often placeholders in all holograms should be refreshed (in ticks)
     * It is not recommended to set too small values for the sake of server performance
     */
    public void setPlaceholdersUpdaterTaskInterval(int newInterval) {
        Utils.cancelTask(placeholdersUpdaterTaskID.intValue());
        placeholdersUpdaterIntervalInTicks = newInterval;
        runPlaceholdersUpdaterTask();
    }

    private void runPlaceholdersUpdaterTask() {
        placeholdersUpdaterTaskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(Hologram hologram : holograms) {
                if(!hologram.isPlaceholdersEnabled()) continue;

                hologram.updateHologramPlaceholders();
            }
        }, 0, placeholdersUpdaterIntervalInTicks));
    }

    private void registerListeners(Listener... listeners) {
        for(Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

}