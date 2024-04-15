package me.kubaw208.data;

import lombok.Getter;
import me.kubaw208.Hologram;
import org.bukkit.Material;

@Getter
public class BlockDisplayData extends Data {

    private Material block;

    public BlockDisplayData(Hologram hologram) {
        super();
        this.hologram = hologram;
        this.setScale(0.25f, 0.25f, 0.25f);
    }

    /**
     * Sets new block for hologram to be displayed
     */
    public BlockDisplayData setBlock(Material block) {
        this.block = block;
        return this;
    }

}