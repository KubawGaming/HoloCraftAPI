package me.kubaw208.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.kubaw208.enums.HologramAlignment;
import me.kubaw208.structs.Hologram;

@Getter @Accessors(chain=true)
public class TextDisplayData extends Data {

    /** text without placeholders */
    @Setter private String text;
    @Setter private int lineWidth;
    @Setter private int backgroundColor;
    @Setter private byte textOpacity;
    @Setter private byte mask;

    public TextDisplayData(Hologram hologram) {
        super();
        this.hologram = hologram;
        this.text = "Default message";
        this.lineWidth = 200;
        this.backgroundColor = 1073741824;
        this.textOpacity = (byte) -1;
        this.mask = (byte) 0;
        this.setScale(0.20f, 0.20f, 0.20f);
    }

    public TextDisplayData setShadowed(boolean shadowed) {
        if(shadowed) mask |= (byte) 0x01;
        else mask &= ~(byte) 0x01;
        return this;
    }

    public TextDisplayData setSeeThrough(boolean seeThrough) {
        if(seeThrough) mask |= (byte) 0x02;
        else mask &= ~(byte) 0x02;
        return this;
    }

    public TextDisplayData setDefaultBackground(boolean defaultBackground) {
        if(defaultBackground) mask |= (byte) 0x04;
        else mask &= ~(byte) 0x04;
        return this;
    }

    public TextDisplayData setAlignment(HologramAlignment alignment) {
        if(alignment == null) return this;

        mask &= ~(byte) 0x08;

        switch(alignment) {
            case LEFT -> mask |= (byte) 0x01;
            case CENTER -> mask |= (byte) 0x00;
            case RIGHT -> mask |= (byte) 0x02;
        }
        return this;
    }

}