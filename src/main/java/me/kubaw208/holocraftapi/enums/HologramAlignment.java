package me.kubaw208.holocraftapi.enums;

import lombok.Getter;

public enum HologramAlignment {

    LEFT(1),
    CENTER(0),
    RIGHT(2);

    @Getter private final int id;

    HologramAlignment(int id) {
        this.id = id;
    }

}