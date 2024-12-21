package me.kubaw208.holocraftapi.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.Material;

@Setter @Getter @Accessors(chain=true)
public class BlockDisplayData extends Data {

    /** Sets new block for hologram to be displayed */
    private Material block;

    public BlockDisplayData(Hologram hologram) {
        super();
        this.hologram = hologram;
        this.setScale(0.25f, 0.25f, 0.25f);
    }

}