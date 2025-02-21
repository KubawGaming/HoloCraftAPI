package me.kubaw208.holocraftapi.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import java.util.List;

public class RemoveEntitiesPacket extends PacketContainer {

    /**
     * Packet which removes entities by their entity IDs.
     */
    public RemoveEntitiesPacket(List<Integer> entityIDs) {
        super(PacketType.Play.Server.ENTITY_DESTROY);

        getModifier().writeDefaults();
        getIntLists().write(0, entityIDs);
    }

}