package it.gamerover.nbs.support.flat.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FlatNoBlackSkyAdapter extends NoBlackSkyAdapter {

    /**
     * isFlat field on the 1.16+ packet is at index 4.
     * Wiki at: https://wiki.vg/Protocol#Respawn
     */
    private static final int LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX = 4;

    /**
     * isFlat field on the 1.16+ packet is at index 4.
     */
    private static final int RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX = 1;

    public FlatNoBlackSkyAdapter(@NotNull Plugin plugin, @NotNull ServerVersion currentVersion) {
        super(plugin, currentVersion);
    }

    @Override
    protected boolean isOverworld(@NotNull World world) {

        World.Environment environment = world.getEnvironment();
        return environment == World.Environment.NORMAL;

    }

    @Override
    protected void editPacket(@NotNull PacketContainer packet, @NotNull PacketType packetType) {

        StructureModifier<Boolean> booleans = packet.getBooleans();

        if (packetType == PacketType.Play.Server.LOGIN) {
            booleans.write(LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
        } else if (packetType == PacketType.Play.Server.RESPAWN) {
            booleans.write(RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
        }

    }

}
