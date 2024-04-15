package me.kubaw208.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class EntitySpawnPacket extends PacketContainer {

    /**
     * Packet which spawns an entity
     */
    public EntitySpawnPacket(int entityId, EntityType entityType, Location location, byte pitch, byte yaw) {
        super(PacketType.Play.Server.SPAWN_ENTITY);

        getModifier().writeDefaults();
        getIntegers().write(0, entityId);
        getUUIDs().write(0, UUID.randomUUID());
        getEntityTypeModifier()
                .write(0, entityType);
        getBytes()
                .write(0, pitch)
                .write(1, yaw);
        getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());
    }

}