package me.kubaw208.listeners.custom;

import lombok.Getter;
import me.kubaw208.Hologram;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HologramTogglePlaceholdersEnabledEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    /** Hologram to which the action of placeholders is toggled */
    @Getter private final Hologram hologram;
    public final boolean switchedTo;

    public HologramTogglePlaceholdersEnabledEvent(Hologram hologram, boolean switchedTo) {
        this.hologram = hologram;
        this.switchedTo = switchedTo;
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