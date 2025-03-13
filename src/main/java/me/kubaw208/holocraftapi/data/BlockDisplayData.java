package me.kubaw208.holocraftapi.data;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;

@Getter @Accessors(chain=true)
public class BlockDisplayData extends Data {

    public BlockDisplayData(Hologram hologram) {
        super(hologram);
        this.hologram = hologram;
        setBlock(getBlockDisplay().getBlock().getMaterial());
    }

    private BlockDisplay getBlockDisplay() {
        return (BlockDisplay) hologram.getEntity();
    }

    public BlockDisplayData setBlock(Material block) {
        getBlockDisplay().setBlock(block.createBlockData());
        return this;
    }

    public Material getBlock() {
        return getBlockDisplay().getBlock().getMaterial();
    }

}