package me.kubaw208.holocraftapi.listeners.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kubaw208.holocraftapi.api.HologramManager;
import me.kubaw208.holocraftapi.utils.Utils;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class HologramChangeTextListener implements PacketListener {

    private final HologramManager hologramManager = HologramManager.getInstance();
    private final static int TEXT_COMPONENT_INDEX = 23;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) return;
        if(!(event.getPlayer() instanceof Player player)) return;

        var wrapper = new WrapperPlayServerEntityMetadata(event);

        if(!hologramManager.getHolograms().containsKey(wrapper.getEntityId())) return;

        Entity entity = hologramManager.getHolograms().get(wrapper.getEntityId()).getEntity();

        if(entity.getType() != EntityType.TEXT_DISPLAY) return;

        for(var metadata : wrapper.getEntityMetadata()) {
            if(metadata.getIndex() != TEXT_COMPONENT_INDEX) continue;

            TextComponent component = (TextComponent) metadata.getValue();

            metadata.setValue(Utils.hexComponent(PlaceholderAPI.setPlaceholders(player, Utils.asText(component))));
        }
    }

}