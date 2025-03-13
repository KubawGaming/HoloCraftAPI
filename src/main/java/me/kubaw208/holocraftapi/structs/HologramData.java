package me.kubaw208.holocraftapi.structs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.kubaw208.holocraftapi.enums.HologramType;
import me.kubaw208.holocraftapi.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Special HologramData class intended to use in files to save hologram data.
 */
@Getter @Setter @Accessors(chain = true)
public class HologramData {

    // Display data
    private final HologramType hologramType;
    private Location location;
    private boolean placeholdersEnabled;
    private boolean publicVisible;
    private HashMap<String, Object> customData = new HashMap<>();
    private Vector3f scale;
    private float rotationYaw;
    private float rotationPitch;
    private Display.Billboard billboard;
    private Display.Brightness brightness;
    private float viewRange;
    private float shadowRadius;
    private float shadowStrength;
    private float width;
    private float height;
    private Color glowColorOverride;
    private int interpolationDuration;
    private int interpolationDelay;

    // TextDisplay data
    private Component text;
    private int lineWidth;
    private byte textOpacity;
    private boolean isShadowed;
    private boolean isSeeThrough;
    private boolean defaultBackground;
    private Color defaultBackgroundColor;
    private TextDisplay.TextAlignment alignment;

    // ItemDisplay data
    private ItemStack item;

    // BlockDisplay data
    private Material block;

    public HologramData(HologramType hologramType, Location location) {
        this.hologramType = hologramType;
        this.location = location;
        this.scale = new Vector3f(1, 1, 1);
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.billboard = Display.Billboard.FIXED;
        this.brightness = null;
        this.viewRange = 1.0f;
        this.shadowRadius = 0.0f;
        this.shadowStrength = 1.0f;
        this.width = 0;
        this.height = 0;
        this.glowColorOverride = Color.WHITE;
        this.interpolationDuration = 0;
        this.interpolationDelay = 0;

        if(hologramType.equals(HologramType.TEXT_DISPLAY)) {
            this.text = Utils.hexComponent("Default message");
            this.lineWidth = 0;
            this.textOpacity = 0;
            this.isShadowed = false;
            this.isSeeThrough = false;
            this.defaultBackground = false;
            this.defaultBackgroundColor = Color.WHITE;
            this.alignment = TextDisplay.TextAlignment.CENTER;
        } else if(hologramType.equals(HologramType.ITEM_DISPLAY)) {
            this.item = new ItemStack(Material.DIRT);
        } else if(hologramType.equals(HologramType.BLOCK_DISPLAY)) {
            this.block = Material.DIRT;
        }
    }

    public HologramData(Hologram hologram) {
        this.hologramType = hologram.getType();
        this.location = hologram.getLocation().clone();

        try {
            this.scale = (Vector3f) hologram.getData().getScale().clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }

        this.rotationYaw = hologram.getData().getRotationYaw();
        this.rotationPitch = hologram.getData().getRotationPitch();
        this.billboard = hologram.getData().getBillboard();
        this.brightness = hologram.getData().getBrightness();
        this.viewRange = hologram.getData().getViewRange();
        this.shadowRadius = hologram.getData().getShadowRadius();
        this.shadowStrength = hologram.getData().getShadowStrength();
        this.width = hologram.getData().getWidth();
        this.height = hologram.getData().getHeight();
        this.glowColorOverride = hologram.getData().getGlowColorOverride();
        this.interpolationDuration = hologram.getData().getInterpolationDuration();
        this.interpolationDelay = hologram.getData().getInterpolationDelay();

        if(hologram.getType().equals(HologramType.TEXT_DISPLAY)) {
            this.text = hologram.getData().asTextDisplayData().getText();
            this.lineWidth = hologram.getData().asTextDisplayData().getLineWidth();
            this.textOpacity = hologram.getData().asTextDisplayData().getTextOpacity();
            this.isShadowed = hologram.getData().asTextDisplayData().isShadowed();
            this.isSeeThrough = hologram.getData().asTextDisplayData().isSeeThrough();
            this.defaultBackground = hologram.getData().asTextDisplayData().isDefaultBackground();
            this.defaultBackgroundColor = hologram.getData().asTextDisplayData().getDefaultBackgroundColor();
            this.alignment = hologram.getData().asTextDisplayData().getAlignment();
        } else if(hologram.getType().equals(HologramType.ITEM_DISPLAY)) {
            this.item = hologram.getData().asItemDisplayData().getItem().clone();
        } else if(hologram.getType().equals(HologramType.BLOCK_DISPLAY)) {
            this.block = hologram.getData().asBlockDisplayData().getBlock();
        }
    }

    public HologramData(HashMap<String, Object> copiedData) {
        this.hologramType = HologramType.valueOf(copiedData.get("type").toString().toUpperCase());

        if(hologramType == null)
            throw new NullPointerException("Hologram type cannot be null! Use HologramType enum to choose correct hologram type.");

        if(copiedData.get("location") != null) {
            HashMap<String, Object> locationData = (HashMap<String, Object>) copiedData.get("location");

            this.location = new Location(
                    Bukkit.getWorld(locationData.get("world").toString()),
                    locationData.get("x") != null ? (double) locationData.get("x") : 0.0,
                    locationData.get("y") != null ? (double) locationData.get("y") : 0.0,
                    locationData.get("z") != null ? (double) locationData.get("z") : 0.0
            );
        }

        if(copiedData.get("publicVisible") != null)
            setPublicVisible((boolean) copiedData.get("publicVisible"));

        if(copiedData.get("placeholdersEnabled") != null)
            setPlaceholdersEnabled((boolean) copiedData.get("placeholdersEnabled"));

        if(copiedData.get("customData") != null)
            setCustomData((HashMap<String, Object>) copiedData.get("customData"));

        if(copiedData.get("data") != null) {
            HashMap<String, Object> data = (HashMap<String, Object>) copiedData.get("data");

            setBillboard(Display.Billboard.valueOf(data.get("billboard").toString().toUpperCase()));

            setViewRange(data.get("viewRange") != null ? Float.parseFloat(data.get("viewRange").toString()) : 1.0f);

            setWidth(data.get("width") != null ? Float.parseFloat(data.get("width").toString()) : 0f);

            setHeight(data.get("height") != null ? Float.parseFloat(data.get("height").toString()) : 0f);

            setRotationYaw(data.get("rotationYaw") != null ? Float.parseFloat(data.get("rotationYaw").toString()) : 0.0f);

            setRotationPitch(data.get("rotationPitch") != null ? Float.parseFloat(data.get("rotationPitch").toString()) : 0.0f);

            HashMap<String, Object> scale = data.get("scale") != null ? (HashMap<String, Object>) data.get("scale") : new HashMap<>();

            setScale(new Vector3f(
                    scale.get("x") != null ? Float.parseFloat(scale.get("x").toString()) : 1.0f,
                    scale.get("y") != null ? Float.parseFloat(scale.get("y").toString()) : 1.0f,
                    scale.get("z") != null ? Float.parseFloat(scale.get("z").toString()) : 1.0f
            ));

            setShadowRadius(
                    data.get("shadowRadius") != null ? Float.parseFloat(data.get("shadowRadius").toString()) : 0
            );

            setShadowStrength(
                    data.get("shadowStrength") != null ? Float.parseFloat(data.get("shadowStrength").toString()) : 1.0f
            );

            setGlowColorOverride(
                    Color.fromRGB(data.get("glowColorOverride") != null ? (int) data.get("glowColorOverride") : 0)
            );

            if(hologramType.equals(HologramType.TEXT_DISPLAY)) {
                HashMap<String, Object> textData = data.get("textData") != null ? (HashMap<String, Object>) data.get("textData") : new HashMap<>();

                setText(Utils.hexComponent(textData.get("text") != null ? textData.get("text").toString() : "Default message"));

                setLineWidth(textData.get("lineWidth") != null ? Integer.parseInt(textData.get("lineWidth").toString()) : 1);

                setTextOpacity(textData.get("textOpacity") != null ? (byte) Integer.parseInt(textData.get("textOpacity").toString()) : (byte) -1);

                setShadowed(textData.get("shadowed") != null ? (boolean) textData.get("shadowed") : false);

                setSeeThrough(textData.get("seeThrough") != null ? (boolean) textData.get("seeThrough") : false);

                setDefaultBackground(textData.get("defaultBackground") != null ? (boolean) textData.get("defaultBackground") : true);

                setDefaultBackgroundColor(textData.get("backgroundColor") != null ? Color.fromRGB((int) textData.get("backgroundColor")) : Color.WHITE);

                setAlignment(textData.get("alignment") != null ? TextDisplay.TextAlignment.valueOf(textData.get("alignment").toString().toUpperCase()) : TextDisplay.TextAlignment.CENTER);
            } else if(hologramType.equals(HologramType.ITEM_DISPLAY)) {
                HashMap<String, Object> itemData = data.get("itemData") != null ? (HashMap<String, Object>) data.get("itemData") : new HashMap<>();

                setItem(itemData.get("item") != null ? new ItemStack(Material.valueOf(itemData.get("item").toString().toUpperCase())) : new ItemStack(Material.DIRT));
            } else if(hologramType.equals(HologramType.BLOCK_DISPLAY)) {
                HashMap<String, Object> blockData = data.get("blockData") != null ? (HashMap<String, Object>) data.get("blockData") : new HashMap<>();

                setBlock(blockData.get("block") != null ? Material.valueOf(blockData.get("block").toString().toUpperCase()) : Material.DIRT);
            }
        }
    }

    public LinkedHashMap<String, Object> getAsMap() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> location = new LinkedHashMap<>();

        location.put("world", getLocation().getWorld().getName());
        location.put("x", getLocation().getX());
        location.put("y", getLocation().getY());
        location.put("z", getLocation().getZ());

        hashMap.put("type", getHologramType().toString());
        hashMap.put("location", location);
        hashMap.put("publicVisible", isPublicVisible());
        hashMap.put("placeholdersEnabled", isPlaceholdersEnabled());
        hashMap.put("customData", getCustomData());
        data.put("billboard", getBillboard().toString());
        data.put("viewRange", getViewRange());
        data.put("width", getWidth());
        data.put("height", getHeight());
        data.put("rotationYaw", getRotationYaw());
        data.put("rotationPitch", getRotationPitch());

        LinkedHashMap<String, Object> scale = new LinkedHashMap<>();

        scale.put("x", getScale().x);
        scale.put("y", getScale().y);
        scale.put("z", getScale().z);
        data.put("scale", scale);

        data.put("shadowRadius", getShadowRadius());
        data.put("shadowStrength", getShadowStrength());
        data.put("glowColorOverride", getGlowColorOverride().asRGB());
        hashMap.put("data", data);

        if(getHologramType().equals(HologramType.TEXT_DISPLAY)) {
            LinkedHashMap<String, Object> textDisplayData = new LinkedHashMap<>();

            textDisplayData.put("text", Utils.asText(getText()));
            textDisplayData.put("lineWidth", getLineWidth());
            textDisplayData.put("textOpacity", getTextOpacity());
            textDisplayData.put("shadowed", isShadowed());
            textDisplayData.put("seeThrough", isSeeThrough());
            textDisplayData.put("defaultBackground", isDefaultBackground());
            textDisplayData.put("backgroundColor", getDefaultBackgroundColor().asRGB());
            textDisplayData.put("alignment", getAlignment().name());
            data.put("textData", textDisplayData);
        } else if(getHologramType().equals(HologramType.ITEM_DISPLAY)) {
            LinkedHashMap<String, Object> itemDisplayData = new LinkedHashMap<>();

            itemDisplayData.put("item", getItem());
            data.put("itemData", itemDisplayData);
        } else if(getHologramType().equals(HologramType.BLOCK_DISPLAY)) {
            LinkedHashMap<String, Object> blockDisplayData = new LinkedHashMap<>();

            blockDisplayData.put("block", getBlock());
            data.put("blockData", blockDisplayData);
        }
        return hashMap;
    }

    public void applyData(Hologram hologram) {
        Display display = (Display) hologram.getEntity();

        if(location != null)
            hologram.setLocation(location.clone());

        hologram.setPlaceholdersEnabled(placeholdersEnabled);
        hologram.setPublicVisible(publicVisible);
        hologram.setCustomData(customData);

        try {
            display.getTransformation().getScale().set((Vector3f) scale.clone());
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }

        display.setRotation(rotationYaw, rotationPitch);
        display.setBrightness(brightness);
        display.setViewRange(viewRange);
        display.setShadowRadius(shadowRadius);
        display.setShadowStrength(shadowStrength);
        display.setDisplayWidth(width);
        display.setDisplayHeight(height);
        display.setGlowColorOverride(glowColorOverride);
        display.setInterpolationDuration(interpolationDuration);
        display.setInterpolationDelay(interpolationDelay);

        if(!hologram.getType().equals(hologramType)) return;

        if(hologramType.equals(HologramType.TEXT_DISPLAY)) {
            hologram.getData().asTextDisplayData()
                    .setText(text)
                    .setLineWidth(lineWidth)
                    .setTextOpacity(textOpacity)
                    .setShadowed(isShadowed)
                    .setSeeThrough(isSeeThrough)
                    .setDefaultBackground(defaultBackground)
                    .setDefaultBackgroundColor(defaultBackgroundColor)
                    .setAlignment(alignment);
        } else if(hologramType.equals(HologramType.ITEM_DISPLAY)) {
            hologram.getData().asItemDisplayData().setItem(item);
        } else if(hologramType.equals(HologramType.BLOCK_DISPLAY)) {
            hologram.getData().asBlockDisplayData().setBlock(block);
        }
    }

}