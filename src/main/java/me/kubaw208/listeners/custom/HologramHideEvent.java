package me.kubaw208.listeners.custom;

import lombok.Getter;
import me.kubaw208.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HologramHideEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final Player player;
    private boolean isCancelled;
    /** Hologram hidden from the player */
    @Getter private final Hologram hologram;
    /** If true, ignores if event is canceled or not and always will hide hologram for player. */
    @Getter private boolean force;

    public HologramHideEvent(Hologram hologram, Player player, boolean force) {
        this.hologram = hologram;
        this.player = player;
        this.force = force;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}