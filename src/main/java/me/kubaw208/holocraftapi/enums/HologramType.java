package me.kubaw208.holocraftapi.enums;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public enum HologramType {

    TEXT_DISPLAY(EntityType.TEXT_DISPLAY),
    BLOCK_DISPLAY(EntityType.BLOCK_DISPLAY),
    ITEM_DISPLAY(EntityType.ITEM_DISPLAY);

    @Getter private final EntityType entityType;

    HologramType(EntityType entityType) {
        this.entityType = entityType;
    }

}