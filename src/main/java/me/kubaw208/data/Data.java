package me.kubaw208.data;

import lombok.Getter;
import me.kubaw208.Hologram;
import me.kubaw208.enums.HologramBillboard;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Represents "Display" metadata in ProtocolLib wiki: https://wiki.vg/Entity_metadata#Display
 */
@Getter
public class Data {

    protected Hologram hologram;
    private int interpolationDelay;
    private int transformationInterpolationDuration;
    private int positionOrRotationInterpolationDuration;
    private final Vector3f translation;
    private final Vector3f scale;
    private final Quaternionf rotation;
    private double xRotation;
    private double yRotation;
    private double zRotation;
    private HologramBillboard billboard;
    private int brightnessOverride;
    private float viewRange;
    private float shadowRadius;
    private float shadowStrength;
    private float width;
    private float height;
    private int glowColorOverride;

    public Data() {
        this.interpolationDelay = 0;
        this.transformationInterpolationDuration = 0;
        this.positionOrRotationInterpolationDuration = 0;
        this.translation = new Vector3f(0f, 0f, 0f);
        this.scale = new Vector3f(1f, 1f, 1f);
        this.rotation = new Quaternionf(0f, 1f, 0f, 1f);
        this.xRotation = 0;
        this.yRotation = 0;
        this.zRotation = 0;
        this.billboard = HologramBillboard.FIXED;
        this.brightnessOverride = -1;
        this.viewRange = 1.0f;
        this.shadowRadius = 0.0f;
        this.shadowStrength = 1.0f;
        this.width = 0.0f;
        this.height = 0.0f;
        this.glowColorOverride = -1;
    }

    /**
     * Lets you get methods for TextDisplay holograms
     * @returns Data as TextDisplayData or null if Data is not TextDisplayData
     */
    public TextDisplayData asTextDisplayData() {
        if(!(this instanceof TextDisplayData textDisplayData)) return null;
        return textDisplayData;
    }

    /**
     * Lets you get methods for BlockDisplay holograms
     * @returns Data as BlockDisplayData or null if Data is not BlockDisplayData
     */
    public BlockDisplayData asBlockDisplayData() {
        if(!(this instanceof BlockDisplayData blockDisplayData)) return null;
        return blockDisplayData;
    }

    /**
     * Lets you get methods for ItemDisplay holograms
     * @returns Data as ItemDisplayData or null if Data is not ItemDisplayData
     */
    public ItemDisplayData asItemDisplayData() {
        if(!(this instanceof ItemDisplayData itemDisplayData)) return null;
        return itemDisplayData;
    }

    public Data setInterpolationDelay(int interpolationDelay) {
        this.interpolationDelay = interpolationDelay;
        return this;
    }

    public Data setTransformation(int transformation) {
        this.transformationInterpolationDuration = transformation;
        return this;
    }

    public Data setPositionOrRotationInterpolationDuration(int positionOrRotationInterpolationDuration) {
        this.positionOrRotationInterpolationDuration = positionOrRotationInterpolationDuration;
        return this;
    }

    public Data setTranslation(Vector3f vector3f) {
        this.translation.set(vector3f);
        return this;
    }

    public Data setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
        return this;
    }

    public Data setBillboard(HologramBillboard billboard) {
        this.billboard = billboard;
        return this;
    }

    /**
     * Sets hologram rotation using degrees
     */
    public Data setRotation(double xRotation, double yRotation, double zRotation) {
        this.xRotation = normalizeAngle(xRotation);
        this.yRotation = normalizeAngle(yRotation);
        this.zRotation = normalizeAngle(zRotation);

        this.rotation.set(new Quaternionf().rotateYXZ(
                (float) Math.toRadians(this.xRotation),
                (float) Math.toRadians(this.yRotation),
                (float) Math.toRadians(this.zRotation)).scale(2));
        return this;
    }

    /**
     * @returns angle in range [0, 360)
     */
    private double normalizeAngle(double angle) {
        angle %= 360;
        if(angle < 0) angle += 360;
        return angle;
    }

    public Data setBrightnessOverride(int brightnessOverride) {
        this.brightnessOverride = brightnessOverride;
        return this;
    }

    public Data setViewRange(float viewRange) {
        this.viewRange = viewRange;
        return this;
    }

    public Data setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public Data setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return this;
    }

    public Data setWidth(float width) {
        this.width = width;
        return this;
    }

    public Data setHeight(float height) {
        this.height = height;
        return this;
    }

    public Data setGlowColorOverride(int glowColorOverride) {
        this.glowColorOverride = glowColorOverride;
        return this;
    }

    public Hologram getHologram() {
        return hologram;
    }

}