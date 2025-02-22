package me.kubaw208.holocraftapi.structs;

import lombok.Getter;
import lombok.Setter;
import me.kubaw208.holocraftapi.data.BlockDisplayData;
import me.kubaw208.holocraftapi.data.Data;
import me.kubaw208.holocraftapi.data.ItemDisplayData;
import me.kubaw208.holocraftapi.data.TextDisplayData;
import me.kubaw208.holocraftapi.enums.HologramBillboard;
import me.kubaw208.holocraftapi.enums.HologramType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

/**
 * A custom class for storing hologram data. Allows you to apply data to the selected hologram.
 */
@Getter @Setter
public class HologramData {

    private HologramType hologramType;
    private Data data;
    private Location location;
    private boolean hideOnUnload = true;
    private boolean placeholdersEnabled = false;
    private boolean publicVisible = false;
    private HashMap<String, Object> customData = new HashMap<>();

    public HologramData(HologramType hologramType, Location location) {
        this.hologramType = hologramType;
        this.location = location;

        switch(hologramType) {
            case TEXT_DISPLAY -> this.data = new TextDisplayData();
            case BLOCK_DISPLAY -> this.data = new BlockDisplayData();
            case ITEM_DISPLAY -> this.data = new ItemDisplayData();
        }
    }

    public HologramData(HashMap<String, Object> copiedData) {
        hologramType = HologramType.valueOf(copiedData.get("hologramType").toString().toUpperCase());

        switch(hologramType) {
            case TEXT_DISPLAY -> this.data = new TextDisplayData();
            case BLOCK_DISPLAY -> this.data = new BlockDisplayData();
            case ITEM_DISPLAY -> this.data = new ItemDisplayData();
        }

        if(copiedData.get("location") != null) {
            HashMap<String, Object> locationData = (HashMap<String, Object>) copiedData.get("location");

            this.location = new Location(
                    Bukkit.getWorld(locationData.get("world").toString()),
                    locationData.get("x") != null ? (double) locationData.get("x") : 0.0,
                    locationData.get("y") != null ? (double) locationData.get("y") : 0.0,
                    locationData.get("z") != null ? (double) locationData.get("z") : 0.0
            );
        }

        if(copiedData.get("hideOnUnload") != null)
            setHideOnUnload((boolean) copiedData.get("hideOnUnload"));

        if(copiedData.get("placeholdersEnabled") != null)
            setPlaceholdersEnabled((boolean) copiedData.get("placeholdersEnabled"));

        if(copiedData.get("publicVisible") != null)
            setPublicVisible((boolean) copiedData.get("publicVisible"));

        if(copiedData.get("customData") != null)
            setCustomData((HashMap<String, Object>) copiedData.get("customData"));

        if(copiedData.get("data") != null) {
            HashMap<String, Object> data = (HashMap<String, Object>) copiedData.get("data");
            HologramBillboard billboard = HologramBillboard.valueOf(data.get("billboard").toString().toUpperCase());

            getData().setBillboard(billboard);

            getData().setViewRange(
                    data.get("data.viewRange") != null ? (int) data.get("data.viewRange") : 1.0f
            );

            getData().setWidth(
                    data.get("data.width") != null ? (int) data.get("data.width") : 0
            );

            getData().setHeight(
                    data.get("data.height") != null ? (int) data.get("data.height") : 0
            );

            getData().setRotation(
                    data.get("data.rotation.x") != null ? (double) data.get("data.rotation.x") : 0.0,
                    data.get("data.rotation.y") != null ? (double) data.get("data.rotation.y") : 0.0,
                    data.get("data.rotation.z") != null ? (double) data.get("data.rotation.z") : 0.0
            );

            getData().setScale(
                    data.get("data.scale.x") != null ? (float) data.get("data.scale.x") : 1.0f,
                    data.get("data.scale.y") != null ? (float) data.get("data.scale.y") : 1.0f,
                    data.get("data.scale.z") != null ? (float) data.get("data.scale.z") : 1.0f
            );

            getData().setShadowRadius(
                    data.get("data.shadowRadius") != null ? (float) data.get("data.shadowRadius") : 0
            );

            getData().setShadowStrength(
                    data.get("data.shadowStrength") != null ? (float) data.get("data.shadowStrength") : 1.0f
            );

            getData().setGlowColorOverride(
                    data.get("data.glowColorOverride") != null ? (int) data.get("data.glowColorOverride") : 1
            );

            if(hologramType.equals(HologramType.TEXT_DISPLAY) && data.get("textData") != null) {
                HashMap<String, Object> textData = (HashMap<String, Object>) data.get("textData");

                getData().asTextDisplayData().setText(
                        textData.get("text") != null ? (String) textData.get("text") : ""
                );

                getData().asTextDisplayData().setLineWidth(
                        textData.get("lineWidth") != null ? (int) textData.get("lineWidth") : 1
                );

                getData().asTextDisplayData().setBackgroundColor(
                        textData.get("backgroundColor") != null ? (int) textData.get("backgroundColor") : 1073741824
                );

                getData().asTextDisplayData().setTextOpacity(
                        textData.get("textOpacity") != null ? (byte) textData.get("textOpacity") : (byte) -1
                );

                getData().asTextDisplayData().setMask(
                        textData.get("mask") != null ? (byte) textData.get("mask") : (byte) 0
                );
            } else if(hologramType.equals(HologramType.ITEM_DISPLAY) && data.get("itemData") != null) {
                HashMap<String, Object> itemData = (HashMap<String, Object>) data.get("itemData");

                getData().asItemDisplayData().setItem(
                        itemData.get("item") != null ? Material.valueOf(itemData.get("item").toString().toUpperCase()) : Material.DIRT
                );
            } else if(hologramType.equals(HologramType.BLOCK_DISPLAY) && data.get("blockData") != null) {
                HashMap<String, Object> blockData = (HashMap<String, Object>) data.get("blockData");

                getData().asBlockDisplayData().setBlock(
                        blockData.get("block") != null ? Material.valueOf(blockData.get("block").toString().toUpperCase()) : Material.DIRT
                );
            }
        }
    }

    public HologramData(Hologram hologram) {
        this.hologramType = hologram.getType();
        this.data = hologram.getData().clone();
        this.location = hologram.getLocation().clone();
        this.hideOnUnload = hologram.isHideOnUnload();
        this.placeholdersEnabled = hologram.isPlaceholdersEnabled();
        this.publicVisible = hologram.isPublicVisible();
        this.customData = hologram.getCustomData();
    }

    /**
     * Sets new data for the given hologram.
     * Incorrect data is ignored (for example, setting data from ItemDisplayData for TextDisplayData).
     * Many changes applied should be reloaded by using hologram.applyChanges(); method.
     */
    public void applyData(Hologram hologram) {
        hologram.setLocation(location.clone());
        hologram.setHideOnLoad(hideOnUnload);
        hologram.setPlaceholdersEnabled(placeholdersEnabled);
        hologram.setPublicVisible(publicVisible);
        hologram.setCustomData((HashMap<String, Object>) customData.clone());

        hologram.getData().setInterpolationDelay(data.getInterpolationDelay());
        hologram.getData().setTransformationInterpolationDuration(data.getTransformationInterpolationDuration());
        hologram.getData().setPositionOrRotationInterpolationDuration(data.getPositionOrRotationInterpolationDuration());
        hologram.getData().setTranslation(data.getTranslation());
        hologram.getData().setScale(data.getScale().x, data.getScale().y, data.getScale().z);
        hologram.getData().setRotation(data.getXRotation(), data.getYRotation(), data.getZRotation());
        hologram.getData().setBillboard(data.getBillboard());
        hologram.getData().setBrightnessOverride(data.getBrightnessOverride());
        hologram.getData().setViewRange(data.getViewRange());
        hologram.getData().setShadowRadius(data.getShadowRadius());
        hologram.getData().setShadowStrength(data.getShadowStrength());
        hologram.getData().setWidth(data.getWidth());
        hologram.getData().setHeight(data.getHeight());
        hologram.getData().setGlowColorOverride(data.getGlowColorOverride());

        if(hologram.getData() instanceof TextDisplayData && data instanceof TextDisplayData) {
            hologram.getData().asTextDisplayData().setText(data.asTextDisplayData().getText());
            hologram.getData().asTextDisplayData().setLineWidth(data.asTextDisplayData().getLineWidth());
            hologram.getData().asTextDisplayData().setBackgroundColor(data.asTextDisplayData().getBackgroundColor());
            hologram.getData().asTextDisplayData().setTextOpacity(data.asTextDisplayData().getTextOpacity());
            hologram.getData().asTextDisplayData().setMask(data.asTextDisplayData().getMask());
        } else if(hologram.getData() instanceof ItemDisplayData && data instanceof ItemDisplayData) {
            hologram.getData().asItemDisplayData().setItem(data.asItemDisplayData().getItem());
        } else if(hologram.getData() instanceof BlockDisplayData && data instanceof BlockDisplayData) {
            hologram.getData().asBlockDisplayData().setBlock(data.asBlockDisplayData().getBlock());
        }
    }

}