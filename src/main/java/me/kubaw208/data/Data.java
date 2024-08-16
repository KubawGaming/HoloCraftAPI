package me.kubaw208.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.kubaw208.enums.HologramBillboard;
import me.kubaw208.structs.Hologram;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Represents "Display" metadata in ProtocolLib wiki: https://wiki.vg/Entity_metadata#Display
 */
@Getter @Accessors(chain=true)
public class Data {

    @Getter protected Hologram hologram;
    @Setter private int interpolationDelay;
    @Setter private int transformationInterpolationDuration;
    @Setter private int positionOrRotationInterpolationDuration;
    @Setter private Vector3f translation;
    private final Vector3f scale;
    private final Quaternionf rotation;
    private double xRotation;
    private double yRotation;
    private double zRotation;
    @Setter private HologramBillboard billboard;
    @Setter private int brightnessOverride;
    @Setter private float viewRange;
    @Setter private float shadowRadius;
    @Setter private float shadowStrength;
    @Setter private float width;
    @Setter private float height;
    @Setter private int glowColorOverride;

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

    public Data setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
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

}