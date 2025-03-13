package me.kubaw208.holocraftapi.data;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

@Getter @Accessors(chain=true)
public class ItemDisplayData extends Data {

    public ItemDisplayData(Hologram hologram) {
        super(hologram);
        this.hologram = hologram;
    }

    private ItemDisplay getItemDisplay() {
        return (ItemDisplay) hologram.getEntity();
    }

    public ItemDisplayData setItem(ItemStack item) {
        getItemDisplay().setItemStack(item);
        return this;
    }

    public ItemDisplayData setItem(Material material) {
        return setItem(new ItemStack(material));
    }

    public ItemStack getItem() {
        return getItemDisplay().getItemStack();
    }

}