package me.kubaw208.holocraftapi.enums;

import lombok.Getter;

@Getter
public enum HologramAlignment {

    LEFT(1),
    CENTER(0),
    RIGHT(2);

    private final int id;

    HologramAlignment(int id) {
        this.id = id;
    }

}