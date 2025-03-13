package me.kubaw208.holocraftapi.structs;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.kubaw208.holocraftapi.api.HologramManager;
import me.kubaw208.holocraftapi.data.BlockDisplayData;
import me.kubaw208.holocraftapi.data.Data;
import me.kubaw208.holocraftapi.data.ItemDisplayData;
import me.kubaw208.holocraftapi.data.TextDisplayData;
import me.kubaw208.holocraftapi.enums.HologramType;
import me.kubaw208.holocraftapi.listeners.custom.HologramChangeLocationEvent;
import me.kubaw208.holocraftapi.listeners.custom.HologramHideEvent;
import me.kubaw208.holocraftapi.listeners.custom.HologramShowEvent;
import me.kubaw208.holocraftapi.listeners.custom.HologramTogglePlaceholdersEnabledEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
public class Hologram {

    @Getter(AccessLevel.NONE) private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private final Plugin plugin = HologramManager.getPlugin();
    /** Current hologram location. */
    private Location location;
    /** Hologram entity ID that is sent to clients. */
    private final Entity entity;
    /** List players who should see specific hologram. */
    private final ArrayList<Player> playersSeeingHologram = new ArrayList<>();
    /** Hologram data that can be applied to hologram. */
    private final Data data;
    /** Hologram type (text, item or block display). */
    private final HologramType type;
    /** Should placeholders work on hologram? */
    private boolean placeholdersEnabled = false;
    /** Should hologram be visible for all players, ignoring getPlayersSeeingHologram() a list? */
    private boolean publicVisible = true;
    /** Custom data for hologram that can be used for any purpose. */
    @Setter private HashMap<String, Object> customData = new HashMap<>();

    public Hologram(HologramType type, Location location) {
        if(type == null) throw new NullPointerException("Hologram type cannot be null! Use HologramType enum to choose correct hologram type.");

        this.type = type;
        this.location = location;

        switch(type) {
            case TEXT_DISPLAY -> {
                entity = location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
                data = new TextDisplayData(this);
            }
            case BLOCK_DISPLAY -> {
                entity = location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
                data = new BlockDisplayData(this);
            }
            case ITEM_DISPLAY -> {
                entity = location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
                data = new ItemDisplayData(this);
            }
            default -> throw new IllegalArgumentException("Unknown hologram type! Use HologramType enum to choose correct hologram type.");
        }

        this.entity.setPersistent(false);
    }

    /**
     * Disables/Enables refreshing placeholders for holograms who don't use placeholders.
     * Disabling it for specific holograms who don't use placeholders may little optimize server performance.
     * Changing isPlaceholdersEnabled() will automatically update the new state for all players.
     * It's default set as false to prevent unnecessary refreshing.
     */
    public Hologram setPlaceholdersEnabled(boolean placeholdersEnabled) {
        HologramTogglePlaceholdersEnabledEvent customEvent = new HologramTogglePlaceholdersEnabledEvent(this, placeholdersEnabled);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        this.placeholdersEnabled = placeholdersEnabled;
        return this;
    }

    /**
     * Shows hologram for a specific player if custom event of showing hologram is not canceled.
     * If a player has seen a hologram before, nothing will be done.
     */
    public Hologram showHologram(Player player) {
        HologramShowEvent customEvent = new HologramShowEvent(this, player, false);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        if(!playersSeeingHologram.contains(player))
            playersSeeingHologram.add(player);

        player.showEntity(plugin, entity);
        return this;
    }

    /**
     * Shows hologram for specific players.
     * @see #showHologram(Player)
     */
    public Hologram showHologram(Player... players) {
        for(Player player : players) {
            showHologram(player);
        }
        return this;
    }

    /**
     * Shows hologram for specific players.
     * @see #showHologram(Player)
     */
    public Hologram showHologram(List<Player> players) {
        for(Player player : players) {
            showHologram(player);
        }
        return this;
    }

    /**
     * Shows hologram for specific players.
     * @see #showHologram(Player)
     */
    public Hologram showHologram(Collection<? extends Player> players) {
        for(Player player : players) {
            showHologram(player);
        }
        return this;
    }

    /**
     * Hides hologram for specific player if custom event of hiding hologram is not canceled.
     * If player has not seen a hologram before, nothing will be done.
     */
    public Hologram hideHologram(Player player) {
        HologramHideEvent customEvent = new HologramHideEvent(this, player, false);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        playersSeeingHologram.remove(player);
        player.hideEntity(plugin, entity);
        return this;
    }

    /**
     * Hides hologram for specific players.
     * @see #hideHologram(Player)
     */
    public Hologram hideHologram(Player... players) {
        for(Player player : players) {
            hideHologram(player);
        }
        return this;
    }

    /**
     * Hides hologram for specific players.
     * @see #hideHologram(Player)
     */
    public Hologram hideHologram(List<Player> players) {
        for(Player player : players) {
            hideHologram(player);
        }
        return this;
    }

    /**
     * Hides hologram for specific players.
     * @see #hideHologram(Player)
     */
    public Hologram hideHologram(Collection<? extends Player> players) {
        for(Player player : players) {
            hideHologram(player);
        }
        return this;
    }

    /**
     * @returns true if player should be able to see hologram. Else returns false.
     */
    public boolean canSee(Player player) {
        return publicVisible || playersSeeingHologram.contains(player);
    }

    /**
     * Sets hologram new location.
     */
    public Hologram setLocation(Location location) {
        HologramChangeLocationEvent customEvent = new HologramChangeLocationEvent(this);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        this.location = location;
        entity.teleport(location);
        return this;
    }

    /**
     * Sets whether the hologram should be visible automatically to every player on the server.
     * After set public visible to true, automatically for all players on the server will be used showPlayer() method.
     * Players joining on the server will see hologram automatically as well.
     * After set public visible to false, automatically for all players on the server will be used hidePlayer() method.
     */
    public Hologram setPublicVisible(boolean publicVisible) {
        if(this.publicVisible == publicVisible) return this;

        this.publicVisible = publicVisible;

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(publicVisible)
                showHologram(player);
            else
                hideHologram(player);
        }
        return this;
    }

    /**
     * Immediately updates all placeholders provided isPlaceholdersEnabled() is enabled.
     * If it's not TEXT_DISPLAY or placeholders are disabled for this hologram, this method will do nothing.
     */
    public Hologram updateHologramPlaceholders() {
        if(!placeholdersEnabled || !type.equals(HologramType.TEXT_DISPLAY)) return this;

        var textDisplay = ((TextDisplay) entity);

        textDisplay.text(textDisplay.text());
        return this;
    }

}