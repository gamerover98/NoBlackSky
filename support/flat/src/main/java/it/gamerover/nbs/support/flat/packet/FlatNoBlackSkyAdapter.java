package it.gamerover.nbs.support.flat.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import it.gamerover.nbs.reflection.util.Comparison;
import it.gamerover.nbs.reflection.util.ReflectionUtil;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("squid:S100") // Rename methods name to match the regular expression '^[a-z][a-zA-Z0-9]*$'.
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

    /**
     * This world type will fix the black sky under height 61.
     * Wiki at: https://wiki.vg/Protocol#Join_Game
     */
    private static final WorldType FLAT_WORLD_TYPE = WorldType.FLAT;

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

        // running server version >= 1.16
        if (isAtLeastNetherUpdate()) {
            editPacketAtLeastNetherUpdate(packet, packetType);
        } else {
            editPacketBeforeNetherUpdate(packet, packetType);
        }

    }

    /**
     * @return True if the running server is at least 1.16.
     */
    private boolean isAtLeastNetherUpdate() {

        Comparison result = ReflectionUtil.compareServerVersions(currentVersion, ServerVersion.V1_16);
        return result == Comparison.SAME || result == Comparison.MAJOR;

    }

    /**
     * Edit the login or respawn packet if the server version is at least 1.16
     *
     * @param packet     The not-null instance of the packet container.
     * @param packetType The not-null instance of the packet type.
     */
    private void editPacketAtLeastNetherUpdate(@NotNull PacketContainer packet, @NotNull PacketType packetType) {

        StructureModifier<Boolean> booleans = packet.getBooleans();

        if (packetType == PacketType.Play.Server.LOGIN) {
            booleans.write(LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
        } else if (packetType == PacketType.Play.Server.RESPAWN) {
            booleans.write(RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
        }

    }

    /**
     * Edit the login or respawn packet if the server version is before 1.16.
     *
     * @param packet     The not-null instance of the packet container.
     * @param packetType The not-null instance of the packet type.
     */
    private void editPacketBeforeNetherUpdate(@NotNull PacketContainer packet, @NotNull PacketType packetType) {

        if (packetType == WrapperPlayServerLogin.TYPE) {

            WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
            wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

        } else if (packetType == WrapperPlayServerRespawn.TYPE) {

            WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
            wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

        }

    }

}
