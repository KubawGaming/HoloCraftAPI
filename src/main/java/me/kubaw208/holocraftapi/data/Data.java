package me.kubaw208.holocraftapi.data;

import me.kubaw208.holocraftapi.structs.Hologram;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Represents "Display" metadata in <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata#Display">ProtocolLib wiki</a>
 */
public class Data {

    protected Hologram hologram;

    public Data(Hologram hologram) {
        this.hologram = hologram;
    }

    /**
     * Lets you get methods for TextDisplay holograms.
     * @returns Data as TextDisplayData or null if Data is not TextDisplayData.
     */
    public TextDisplayData asTextDisplayData() {
        if(!(this instanceof TextDisplayData textDisplayData)) return null;
        return textDisplayData;
    }

    /**
     * Lets you get methods for BlockDisplay holograms.
     * @returns Data as BlockDisplayData or null if Data is not BlockDisplayData.
     */
    public BlockDisplayData asBlockDisplayData() {
        if(!(this instanceof BlockDisplayData blockDisplayData)) return null;
        return blockDisplayData;
    }

    /**
     * Lets you get methods for ItemDisplay holograms.
     * @returns Data as ItemDisplayData or null if Data is not ItemDisplayData.
     */
    public ItemDisplayData asItemDisplayData() {
        if(!(this instanceof ItemDisplayData itemDisplayData)) return null;
        return itemDisplayData;
    }

    private Display getDisplay() {
        return (Display) hologram.getEntity();
    }

    /**
     * @returns angle in range [0, 360).
     */
    private double normalizeAngle(double angle) {
        angle %= 360;

        if(angle < 0)
            angle += 360;

        return angle;
    }

    public Data setScale(float x, float y, float z) {
        getDisplay().getTransformation().getScale().set(x, y, z);
        return this;
    }

    public Vector3f getScale() {
        return getDisplay().getTransformation().getScale();
    }

    public Data setRotation(float x, float y, float z) {
        Transformation transformation = getDisplay().getTransformation();
        Quaternionf rotation = new Quaternionf().rotateXYZ(
                (float) Math.toRadians(normalizeAngle(x)),
                (float) Math.toRadians(normalizeAngle(y)),
                (float) Math.toRadians(normalizeAngle(z))
        );
        transformation.getRightRotation().set(rotation);
        getDisplay().setTransformation(transformation);
        return this;
    }

    public float getRotationYaw() {
        return getDisplay().getYaw();
    }

    public float getRotationPitch() {
        return getDisplay().getPitch();
    }

    public Data setBillboard(Display.Billboard billboard) {
        getDisplay().setBillboard(billboard);
        return this;
    }

    public Display.Billboard getBillboard() {
        return getDisplay().getBillboard();
    }

    public Data setBrightness(int blockLight, int skyLight) {
        getDisplay().setBrightness(new Display.Brightness(blockLight, skyLight));
        return this;
    }

    public Display.Brightness getBrightness() {
        return getDisplay().getBrightness();
    }

    public Data setViewRange(float viewRange) {
        getDisplay().setViewRange(viewRange);
        return this;
    }

    public float getViewRange() {
        return getDisplay().getViewRange();
    }

    public Data setShadowRadius(float shadowRadius) {
        getDisplay().setShadowRadius(shadowRadius);
        return this;
    }

    public float getShadowRadius() {
        return getDisplay().getShadowRadius();
    }

    public Data setShadowStrength(float shadowStrength) {
        getDisplay().setShadowStrength(shadowStrength);
        return this;
    }

    public float getShadowStrength() {
        return getDisplay().getShadowStrength();
    }

    public Data setWidth(float width) {
        getDisplay().setDisplayWidth(width);
        return this;
    }

    public float getWidth() {
        return getDisplay().getDisplayWidth();
    }

    public Data setHeight(float height) {
        getDisplay().setDisplayHeight(height);
        return this;
    }

    public float getHeight() {
        return getDisplay().getDisplayHeight();
    }

    public Data setGlowColorOverride(Color color) {
        getDisplay().setGlowColorOverride(color);
        return this;
    }

    public Color getGlowColorOverride() {
        return getDisplay().getGlowColorOverride();
    }

    public Data setInterpolationDuration(int duration) {
        getDisplay().setInterpolationDuration(duration);
        return this;
    }

    public int getInterpolationDuration() {
        return getDisplay().getInterpolationDuration();
    }

    public Data setInterpolationDelay(int delay) {
        getDisplay().setInterpolationDelay(delay);
        return this;
    }

    public int getInterpolationDelay() {
        return getDisplay().getInterpolationDelay();
    }

    public Data setTeleportDuration(int delay) {
        getDisplay().setTeleportDuration(delay);
        return this;
    }

    public int getTeleportDuration() {
        return getDisplay().getTeleportDuration();
    }

}