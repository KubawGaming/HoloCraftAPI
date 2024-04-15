package me.kubaw208.data;

import lombok.Getter;
import me.kubaw208.Hologram;
import org.bukkit.Material;

@Getter
public class ItemDisplayData extends Data {

    private Material item;

    public ItemDisplayData(Hologram hologram) {
        super();
        this.hologram = hologram;
        this.setScale(0.5f, 0.5f, 0.5f);
    }

    /**
     * Sets new item for hologram to be displayed
     */
    public ItemDisplayData setItem(Material item) {
        this.item = item;
        return this;
    }

}