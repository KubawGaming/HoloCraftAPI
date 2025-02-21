package me.kubaw208.holocraftapi.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kubaw208.holocraftapi.structs.Hologram;
import me.kubaw208.holocraftapi.data.BlockDisplayData;
import me.kubaw208.holocraftapi.data.Data;
import me.kubaw208.holocraftapi.data.ItemDisplayData;
import me.kubaw208.holocraftapi.data.TextDisplayData;
import me.kubaw208.holocraftapi.utils.Utils;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityMetadataPacket extends PacketContainer {

    /**
     * Sets metadata for given hologram.
     * @param receiver - player who will get this packet.
     * @param hologram - from that hologram will be got his data and send to receiver.
     */
    public EntityMetadataPacket(Player receiver, Hologram hologram) {
        super(PacketType.Play.Server.ENTITY_METADATA);

        Data data = hologram.getData();
        List<WrappedDataValue> values = new ArrayList<>(getDisplayDataWrappersList(data));

        getModifier().writeDefaults();
        getIntegers().write(0, hologram.getEntityID());

        if(data instanceof TextDisplayData textDisplayData) {
            values.add(
                    new WrappedDataValue(
                            23, //text
                            WrappedDataWatcher.Registry.getChatComponentSerializer(),
                            WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(Utils.hexComponent(
                                    hologram.isPlaceholdersEnabled() ? PlaceholderAPI.setPlaceholders(receiver, textDisplayData.getText()) : textDisplayData.getText()
                            ))).getHandle()
                    ));
        } else if(data instanceof BlockDisplayData blockDisplayData) {
            values.add(
                    new WrappedDataValue(
                            23, //block
                            WrappedDataWatcher.Registry.get(MinecraftReflection.getIBlockDataClass()),
                            WrappedBlockData.createData(blockDisplayData.getBlock()).getHandle()
                    ));
        } else if(data instanceof ItemDisplayData itemDisplayData) {
            values.add(
                    new WrappedDataValue(
                            23, //block
                            WrappedDataWatcher.Registry.get(MinecraftReflection.getItemStackClass()),
                            MinecraftReflection.getMinecraftItemStack(new ItemStack(itemDisplayData.getItem()))
                    ));
        }

        getDataValueCollectionModifier().write(0, values);
    }

    /**
     * Constructor created for TEXT_DISPLAY to update the package with only hologram text for players.
     * Don't use it for BlockDisplay or ItemDisplay!
     */
    public EntityMetadataPacket(int entityID, Player receiver, String text, boolean placeholdersEnabled) {
        super(PacketType.Play.Server.ENTITY_METADATA);

        getModifier().writeDefaults();
        getIntegers().write(0, entityID);

        List<WrappedDataValue> values = new ArrayList<>(List.of(
                new WrappedDataValue(
                        23, //text
                        WrappedDataWatcher.Registry.getChatComponentSerializer(),
                        WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(Utils.hexComponent(
                                placeholdersEnabled ? PlaceholderAPI.setPlaceholders(receiver, text) : text
                        ))).getHandle()
                )));

        getDataValueCollectionModifier().write(0, values);
    }

    /**
     * Display data values you can find <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata#Display">here</a>
     * @returns List of WrappedDataValue
     */
    public List<WrappedDataValue> getDisplayDataWrappersList(Data data) {
        return List.of(
                new WrappedDataValue(
                        8, // Interpolation delay
                        WrappedDataWatcher.Registry.get(Integer.class),
                        data.getInterpolationDelay()
                ),
                new WrappedDataValue(
                        9, // Transformation interpolation duration
                        WrappedDataWatcher.Registry.get(Integer.class),
                        data.getTransformationInterpolationDuration()
                ),
                new WrappedDataValue(
                        10, // Position/Rotation interpolation duration
                        WrappedDataWatcher.Registry.get(Integer.class),
                        data.getPositionOrRotationInterpolationDuration()
                ),
                new WrappedDataValue(
                        11, // Translation (offset)
                        WrappedDataWatcher.Registry.get(Vector3f.class),
                        data.getTranslation()
                ),
                new WrappedDataValue(
                        12, // Scale
                        WrappedDataWatcher.Registry.get(Vector3f.class),
                        data.getScale()
                ),
                new WrappedDataValue(
                        13, // Rotation left
                        WrappedDataWatcher.Registry.get(Quaternionf.class),
                        data.getRotation()
                ),
                new WrappedDataValue(
                        15, // Billboard constraints
                        WrappedDataWatcher.Registry.get(Byte.class),
                        (byte) data.getBillboard().getId()
                ),
                new WrappedDataValue(
                        16, // Brightness override
                        WrappedDataWatcher.Registry.get(Integer.class),
                        data.getBrightnessOverride()
                ),
                new WrappedDataValue(
                        17, // View range
                        WrappedDataWatcher.Registry.get(Float.class),
                        data.getViewRange()
                ),
                new WrappedDataValue(
                        18, // Shadow radius
                        WrappedDataWatcher.Registry.get(Float.class),
                        data.getShadowRadius()
                ),
                new WrappedDataValue(
                        19, // Shadow strength
                        WrappedDataWatcher.Registry.get(Float.class),
                        data.getShadowStrength()
                ),
                new WrappedDataValue(
                        20, // Width
                        WrappedDataWatcher.Registry.get(Float.class),
                        data.getWidth()
                ),
                new WrappedDataValue(
                        21, // Height
                        WrappedDataWatcher.Registry.get(Float.class),
                        data.getHeight()
                ),
                new WrappedDataValue(
                        22, // Glow color override
                        WrappedDataWatcher.Registry.get(Integer.class),
                        data.getGlowColorOverride()
                )
        );
    }

}