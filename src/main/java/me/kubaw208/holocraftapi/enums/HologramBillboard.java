package me.kubaw208.holocraftapi.enums;

import lombok.Getter;

@Getter
public enum HologramBillboard {

    FIXED(0),
    VERTICAL(1),
    HORIZONTAL(2),
    CENTERED(3);

    private final int id;

    HologramBillboard(int id) {
        this.id = id;
    }

}