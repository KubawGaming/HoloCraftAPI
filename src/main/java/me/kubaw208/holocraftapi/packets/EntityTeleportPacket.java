package me.kubaw208.holocraftapi.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;

public class EntityTeleportPacket extends PacketContainer {

    /**
     * Packet which teleports an entity to new coordinates.
     */
    public EntityTeleportPacket(int entityId, Location newLocation) {
        super(PacketType.Play.Server.ENTITY_TELEPORT);

        getModifier().writeDefaults();
        getIntegers().write(0, entityId);
        getDoubles().write(0, newLocation.getX());
        getDoubles().write(1, newLocation.getY());
        getDoubles().write(2, newLocation.getZ());
        getBooleans().write(0, true);
    }

}