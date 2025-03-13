package me.kubaw208.holocraftapi.data;

import me.kubaw208.holocraftapi.structs.Hologram;
import me.kubaw208.holocraftapi.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;

public class TextDisplayData extends Data {

    public TextDisplayData(Hologram hologram) {
        super(hologram);
        this.hologram = hologram;
    }

    private TextDisplay getTextDisplay() {
        return (TextDisplay) hologram.getEntity();
    }

    public TextDisplayData setText(Component text) {
        getTextDisplay().text(text);
        return this;
    }

    public TextDisplayData setText(String text) {
        getTextDisplay().text(Utils.hexComponent(text));
        return this;
    }

    public Component getText() {
        return getTextDisplay().text();
    }

    public TextDisplayData setLineWidth(int lineWidth) {
        getTextDisplay().setLineWidth(lineWidth);
        return this;
    }

    public int getLineWidth() {
        return getTextDisplay().getLineWidth();
    }

    public TextDisplayData setTextOpacity(byte textOpacity) {
        getTextDisplay().setTextOpacity(textOpacity);
        return this;
    }

    public byte getTextOpacity() {
        return getTextDisplay().getTextOpacity();
    }

    public TextDisplayData setShadowed(boolean shadowed) {
        getTextDisplay().setShadowed(shadowed);
        return this;
    }

    public boolean isShadowed() {
        return getTextDisplay().isShadowed();
    }

    public TextDisplayData setSeeThrough(boolean seeThrough) {
        getTextDisplay().setSeeThrough(seeThrough);
        return this;
    }

    public boolean isSeeThrough() {
        return getTextDisplay().isSeeThrough();
    }

    public TextDisplayData setDefaultBackground(boolean defaultBackground) {
        getTextDisplay().setDefaultBackground(defaultBackground);
        return this;
    }

    public boolean isDefaultBackground() {
        return getTextDisplay().isDefaultBackground();
    }

    public TextDisplayData setDefaultBackgroundColor(Color backgroundColor) {
        getTextDisplay().setBackgroundColor(backgroundColor);
        return this;
    }

    public Color getDefaultBackgroundColor() {
        return getTextDisplay().getBackgroundColor();
    }

    public TextDisplayData setAlignment(TextDisplay.TextAlignment alignment) {
        getTextDisplay().setAlignment(alignment);
        return this;
    }

    public TextDisplay.TextAlignment getAlignment() {
        return getTextDisplay().getAlignment();
    }

}