package me.kubaw208.holocraftapi.listeners.custom;

import lombok.Getter;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HologramApplyChangesEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    /** Hologram to which changes have been updated. */
    @Getter private final Hologram hologram;

    public HologramApplyChangesEvent(Hologram hologram) {
        this.hologram = hologram;
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