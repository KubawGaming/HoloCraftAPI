package me.kubaw208.holocraftapi.structs;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.kubaw208.holocraftapi.data.BlockDisplayData;
import me.kubaw208.holocraftapi.data.Data;
import me.kubaw208.holocraftapi.data.ItemDisplayData;
import me.kubaw208.holocraftapi.data.TextDisplayData;
import me.kubaw208.holocraftapi.enums.HologramType;
import me.kubaw208.holocraftapi.listeners.custom.*;
import me.kubaw208.holocraftapi.packets.EntityMetadataPacket;
import me.kubaw208.holocraftapi.packets.EntitySpawnPacket;
import me.kubaw208.holocraftapi.packets.EntityTeleportPacket;
import me.kubaw208.holocraftapi.packets.RemoveEntitiesPacket;
import me.kubaw208.holocraftapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
public class Hologram {

    @Getter(AccessLevel.NONE) private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    /** Previous hologram location. If hologram is just spawned, the previous location will have the same location as getLocation(). */
    private Location previousLocation;
    /** Current hologram location. */
    private Location location;
    /** Hologram entity ID that is sent to clients. */
    private final int entityID;
    /** List players who should see specific hologram. */
    private final ArrayList<Player> playersSeeingHologram = new ArrayList<>();
    /** Hologram data that can be applied to hologram. */
    private final Data data;
    /** Hologram type (text, item or block display). */
    private final HologramType type;
    /**
     * If true, removes hologram after plugin reload to prevent visibility of holograms without possibility of removal for players.
     * If a developer wants to keep a hologram visible (for example, saved a Hologram object and still can operate on it),
     * he should set this Boolean to false which should prevent automatically deleting hologram from the world.
     * @WARNING After plugin load, holograms list in ProtocolManager will be cleared!
     * This can cause errors, for example, if a player changes world, holograms won't be re-showed for him!
     * If you want to keep a Hologram object after plugin reload, you should add Hologram to the list in the HologramManager again.
     */
    private boolean hideOnUnload = true;
    /** Should placeholders work on hologram? */
    private boolean placeholdersEnabled = false;
    /** Should hologram be visible for all players, ignoring getPlayersSeeingHologram() a list? */
    private boolean publicVisible = false;
    /** Custom data for hologram that can be used for any purpose. */
    @Setter private HashMap<String, Object> customData = new HashMap<>();

    public Hologram(HologramType type, Location location) {
        if(type == null) throw new NullPointerException("Hologram type cannot be null! Use HologramType enum to choose correct hologram type.");

        this.entityID = Utils.getRandom(1000000, 999999999);
        this.type = type;
        this.location = location;
        this.previousLocation = location;

        switch(type) {
            case TEXT_DISPLAY -> data = new TextDisplayData(this);
            case BLOCK_DISPLAY -> data = new BlockDisplayData(this);
            case ITEM_DISPLAY -> data = new ItemDisplayData(this);
            default -> data = null;
        }
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

        for(Player player : playersSeeingHologram) {
            if(!location.getWorld().equals(player.getWorld())) continue;

            protocolManager.sendServerPacket(player, new EntityMetadataPacket(entityID, player, data.asTextDisplayData().getText(), placeholdersEnabled));
        }
        return this;
    }

    /**
     * Applies changes made in hologram data for each player seeing hologram.
     */
    public void applyChanges() {
        HologramApplyChangesEvent customEvent = new HologramApplyChangesEvent(this);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return;

        PacketContainer packet = previousLocation.getWorld().equals(location.getWorld()) ?
                new EntityTeleportPacket(entityID, location) :
                new EntitySpawnPacket(entityID, type.getEntityType(), location, (byte) 0, (byte) 0);

        for(Player player : playersSeeingHologram) {
            if(!player.getWorld().equals(location.getWorld())) {
                if(player.getWorld().equals(previousLocation.getWorld())) {
                    protocolManager.sendServerPacket(player, new RemoveEntitiesPacket(List.of(entityID)));
                }
                continue; //Not need to send hologram packet for player if he is in other world
            }

            protocolManager.sendServerPacket(player, packet);
            protocolManager.sendServerPacket(player, new EntityMetadataPacket(player, this));
        }
    }

    /**
     * Shows hologram for a specific player if custom event of showing hologram is not canceled.
     * If a player has seen a hologram before, nothing will be done.
     */
    public Hologram showHologram(Player player) {
        if(playersSeeingHologram.contains(player)) return this;

        HologramShowEvent customEvent = new HologramShowEvent(this, player, false);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        playersSeeingHologram.add(player);

        if(player.getWorld().equals(location.getWorld())) { //Not need to send hologram packet for player if he is in other world
            protocolManager.sendServerPacket(player, new EntitySpawnPacket(entityID, type.getEntityType(), location, (byte) 0, (byte) 0));
            protocolManager.sendServerPacket(player, new EntityMetadataPacket(player, this));
        }
        return this;
    }

    /**
     * Shows hologram for specific player.
     * @param force - if true, ignores if event is canceled and forcing to send a packet to show hologram for player if he is on the same world as hologram.
     */
    public Hologram showHologram(Player player, boolean force) {
        if(!force && playersSeeingHologram.contains(player)) return this;

        HologramShowEvent customEvent = new HologramShowEvent(this, player, force);

        customEvent.callEvent();

        if(!force && customEvent.isCancelled()) return this;

        if(!playersSeeingHologram.contains(player)) playersSeeingHologram.add(player);

        if(player.getWorld().equals(location.getWorld())) { //Not need to send hologram packet for player if he is in other world
            protocolManager.sendServerPacket(player, new EntitySpawnPacket(entityID, type.getEntityType(), location, (byte) 0, (byte) 0));
            protocolManager.sendServerPacket(player, new EntityMetadataPacket(player, this));
        }
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
     * @see #showHologram(Player, boolean)
     */
    public Hologram showHologram(boolean force, Player... players) {
        for(Player player : players) {
            showHologram(player, force);
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
     * @see #showHologram(Player, boolean)
     */
    public Hologram showHologram(List<Player> players, boolean force) {
        for(Player player : players) {
            showHologram(player, force);
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
     * Shows hologram for specific players.
     * @see #showHologram(Player)
     */
    public Hologram showHologram(Collection<? extends Player> players, boolean force) {
        for(Player player : players) {
            showHologram(player, force);
        }
        return this;
    }

    /**
     * Hides hologram for specific player if custom event of hiding hologram is not canceled.
     * If player has not seen a hologram before, nothing will be done.
     */
    public Hologram hideHologram(Player player) {
        if(!playersSeeingHologram.contains(player)) return this;

        HologramHideEvent customEvent = new HologramHideEvent(this, player, false);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        playersSeeingHologram.remove(player);

        if(player.getWorld().equals(location.getWorld())) { //Not need to send hologram packet for player if he is in other world
            protocolManager.sendServerPacket(player, new RemoveEntitiesPacket(List.of(entityID)));
        }
        return this;
    }

    /**
     * Hides hologram for specific player.
     * @param force - if true, ignores if event is canceled and forcing to send a packet to hide hologram for player if he is on the same world as hologram.
     */
    public Hologram hideHologram(Player player, boolean force) {
        if(!force && !playersSeeingHologram.contains(player)) return this;

        HologramHideEvent customEvent = new HologramHideEvent(this, player, force);

        customEvent.callEvent();

        if(!force && customEvent.isCancelled()) return this;

        playersSeeingHologram.remove(player);

        if(player.getWorld().equals(location.getWorld())) { //Not need to send hologram packet for player if he is in other world
            protocolManager.sendServerPacket(player, new RemoveEntitiesPacket(List.of(entityID)));
        }
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
     * @see #hideHologram(Player, boolean)
     */
    public Hologram hideHologram(boolean force, Player... players) {
        for(Player player : players) {
            hideHologram(player, force);
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
     * @see #hideHologram(Player, boolean)
     */
    public Hologram hideHologram(List<Player> players, boolean force) {
        for(Player player : players) {
            hideHologram(player, force);
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
     * Hides hologram for specific players.
     * @see #hideHologram(Player, boolean)
     */
    public Hologram hideHologram(Collection<? extends Player> players, boolean force) {
        for(Player player : players) {
            hideHologram(player, force);
        }
        return this;
    }

    /**
     * @returns true if player can see hologram. Else returns false.
     */
    public boolean canSee(Player player) {
        return playersSeeingHologram.contains(player);
    }

    /**
     * Immediately updates all placeholders provided isPlaceholdersEnabled() is enabled.
     * If it's not TEXT_DISPLAY or placeholders are disabled for this hologram, this method will do nothing.
     */
    public Hologram updateHologramPlaceholders() {
        if(!placeholdersEnabled || !type.equals(HologramType.TEXT_DISPLAY)) return this;

        for(Player player : playersSeeingHologram) {
            if(!location.getWorld().equals(player.getWorld())) continue;

            protocolManager.sendServerPacket(player, new EntityMetadataPacket(entityID, player, data.asTextDisplayData().getText(), true));
        }
        return this;
    }

    /**
     * Sets hologram new location.
     */
    public Hologram setLocation(Location location) {
        HologramChangeLocationEvent customEvent = new HologramChangeLocationEvent(this);

        customEvent.callEvent();

        if(customEvent.isCancelled()) return this;

        this.previousLocation = this.location;
        this.location = location;
        return this;
    }

    /**
     * Sets whether to automatically remove the hologram when the plugin is reloaded
     */
    public Hologram setHideOnLoad(boolean hideOnLoad) {
        this.hideOnUnload = hideOnLoad;
        return this;
    }

    /**
     * Sets whether the hologram should be visible automatically to every player on the server.
     * After set public visible to true, automatically for all players on the server will be used showPlayer() method.
     * Players joining on the server will see hologram automatically as well.
     * @WARNING This method won't hide players automatically after set it to false! If you need to hide hologram from players, you should loop through playersSeeingHologram and call hideHologram().
     */
    public Hologram setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;

        if(publicVisible) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                showHologram(player);
            }
        }
        return this;
    }

}