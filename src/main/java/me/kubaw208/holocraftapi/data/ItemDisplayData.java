package me.kubaw208.holocraftapi.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.Material;

@Setter @Getter @Accessors(chain=true)
public class ItemDisplayData extends Data {

    /** Sets new item for hologram to be displayed */
    private Material item;

    public ItemDisplayData(Hologram hologram) {
        super();
        this.hologram = hologram;
        this.setScale(0.5f, 0.5f, 0.5f);
    }

}