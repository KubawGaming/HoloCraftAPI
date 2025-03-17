package me.kubaw208.holocraftapi.api;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;
import me.kubaw208.holocraftapi.enums.HologramType;
import me.kubaw208.holocraftapi.listeners.PlayerJoinListener;
import me.kubaw208.holocraftapi.listeners.PlayerQuitListener;
import me.kubaw208.holocraftapi.listeners.PluginDisableListener;
import me.kubaw208.holocraftapi.listeners.WorldDeleteListener;
import me.kubaw208.holocraftapi.listeners.custom.HologramCreateEvent;
import me.kubaw208.holocraftapi.listeners.custom.HologramDeleteEvent;
import me.kubaw208.holocraftapi.listeners.packet.HologramChangeTextListener;
import me.kubaw208.holocraftapi.structs.Hologram;
import me.kubaw208.holocraftapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HologramManager {

    @Getter private static Plugin plugin;
    @Getter private static HologramManager instance;
    /**
     * List where you can find instances of holograms.
     * You won't find holograms removed after reloading the plugin.
     * If you want those holograms to continue working properly, you need to add them to this list again.
     * @Warning Don't make changes to the list if you're not sure what you're doing!
     */
    @Getter private final HashMap<Integer, Hologram> holograms = new HashMap<>();
    private final AtomicInteger placeholdersUpdaterTaskID = new AtomicInteger();
    private int placeholdersUpdaterIntervalInTicks;
    private final ArrayList<PacketListener> registeredPacketListeners = new ArrayList<>();
    private final ArrayList<PacketListenerCommon> registeredPacketCommonListeners = new ArrayList<>();

    public HologramManager(Plugin plugin) {
        HologramManager.plugin = plugin;
        instance = this;

        registerListeners(new PlayerJoinListener(), new PlayerQuitListener(), new WorldDeleteListener(), new PluginDisableListener());
        registerPacketListeners(PacketListenerPriority.NORMAL, new HologramChangeTextListener());
        setPlaceholdersUpdaterTaskInterval(200); // Default refresh placeholders in all holograms every 10 seconds
    }

    /**
     * Creates new hologram depending on a hologram type.
     * @returns new hologram instance or null if custom event of creating hologram is canceled.
     */
    public Hologram createHologram(HologramType hologramType, Location location) {
        Hologram hologram = new Hologram(hologramType, location);
        HologramCreateEvent customEvent = new HologramCreateEvent(hologram);

        customEvent.callEvent();

        if(customEvent.isCancelled()) {
            if(!hologram.getEntity().isDead())
                hologram.getEntity().remove();
            return null;
        }

        holograms.put(
                customEvent.getHologram().getEntity().getEntityId(),
                customEvent.getHologram());

        return customEvent.getHologram();
    }

    /**
     * Deletes hologram and automatically removes it from players visibility if custom event of deleting hologram is not canceled.
     */
    public void deleteHologram(Hologram hologram) {
        HologramDeleteEvent customEvent = new HologramDeleteEvent(hologram);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return;

        int entityID = hologram.getEntity().getEntityId();

        if(!hologram.getEntity().isDead())
            hologram.getEntity().remove();

        holograms.remove(entityID);
    }

    /**
     * Deletes multiple holograms.
     * @see #deleteHologram(Hologram)
     */
    public void deleteHolograms(List<Hologram> holograms) {
        for(Hologram hologram : holograms) {
            this.deleteHologram(hologram);
        }
    }

    /**
     * Deletes multiple holograms.
     * @see #deleteHologram(Hologram)
     */
    public void deleteHolograms(Collection<Hologram> holograms) {
        for(Hologram hologram : holograms) {
            this.deleteHologram(hologram);
        }
    }

    /**
     * Sets how often placeholders in all holograms should be refreshed (in ticks).
     * It is not recommended to set too small values for the sake of server performance.
     * When setting the value, take into account the number of holograms.
     */
    public void setPlaceholdersUpdaterTaskInterval(int newInterval) {
        Utils.cancelTask(placeholdersUpdaterTaskID.intValue());
        placeholdersUpdaterIntervalInTicks = newInterval;
        runPlaceholdersUpdaterTask();
    }

    private void runPlaceholdersUpdaterTask() {
        placeholdersUpdaterTaskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(Hologram hologram : holograms.values()) {
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

    public void registerPacketListeners(PacketListenerPriority priority, PacketListener... listeners) {
        for(PacketListener listener : listeners) {
            if(!this.registeredPacketListeners.contains(listener)) {
                PacketListenerCommon packetCommonListener = PacketEvents.getAPI().getEventManager().registerListener(listener, priority);
                this.registeredPacketCommonListeners.add(packetCommonListener);
                this.registeredPacketListeners.add(listener);
            }
        }
    }

    public void unregisterPacketListeners() {
        for(PacketListenerCommon listener : this.registeredPacketCommonListeners) {
            PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        }

        this.registeredPacketListeners.clear();
        this.registeredPacketCommonListeners.clear();
    }

}