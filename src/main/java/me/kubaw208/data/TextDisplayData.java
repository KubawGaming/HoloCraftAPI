package me.kubaw208.data;

import lombok.Getter;
import me.kubaw208.Hologram;
import me.kubaw208.enums.HologramAlignment;

@Getter
public class TextDisplayData extends Data {

    /** text without placeholders */
    private String text;
    private int lineWidth;
    private int backgroundColor;
    private byte textOpacity;
    private byte mask;

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

    public TextDisplayData setText(String text) {
        this.text = text;
        return this;
    }

    public TextDisplayData setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public TextDisplayData setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextDisplayData setTextOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

    public TextDisplayData setMask(byte mask) {
        this.mask = mask;
        return this;
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