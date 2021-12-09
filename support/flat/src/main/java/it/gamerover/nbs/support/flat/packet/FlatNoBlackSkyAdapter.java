package it.gamerover.nbs.support.flat.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.dumptruckman.minecraft.util.Logging;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import it.gamerover.nbs.reflection.util.Comparison;
import it.gamerover.nbs.reflection.util.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

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

    /**
     * Up to 1.15.x, the join and respawn packet has a dimension
     * parameter with three possible values:
     *  -1: nether
     *   0: overworld (normal world)
     *   1: end
     */
    private static final int OVERWORLD_DIMENSION = 0;

    public FlatNoBlackSkyAdapter(@NotNull Plugin plugin, @NotNull ServerVersion currentVersion) {
        super(plugin, currentVersion);
    }

    @Override
    protected boolean isOverworld(@NotNull PacketContainer packet) {

        // running server version >= 1.16
        if (isAtLeastNetherUpdate()) {
            return checkWorldAtLeastNetherUpdate(packet);
        } else {
            return checkWorldBeforeNetherUpdate(packet);
        }

    }

    @Nullable
    @Override
    protected World getWorld(@Nullable Player player, @NotNull PacketContainer packet) {

        World result = null;

        if (isAtLeastNetherUpdate()) {

            StructureModifier<World> worldsStructureModifier = packet.getWorldKeys();
            List<World> worlds = worldsStructureModifier.getValues();
            Optional<World> optionalWorld = worlds.stream().findAny();

            if (optionalWorld.isPresent()) {
                result = optionalWorld.get();
            }

        }

        if (result == null && player != null) {

            /*
             * Trying to fix the exception "The method getWorld is not supported for temporary players":
             * https://github.com/gamerover98/NoBlackSky/issues/4
             */
            try {

                result = player.getWorld();

            } catch (Exception ex) {
                // if it fails, the black sky fix doesn't work for the current logging player.
            }

        }

        if (isDebugMode()) {

            String worldName = null;

            if (result != null) {
                worldName = result.getName();
            }

            Logging.finest(ChatColor.AQUA + "Packet-world: %s", worldName);

        }

        return result;

    }

    @Override
    protected void editPacket(@NotNull PacketContainer packet) {

        // running server version >= 1.16
        if (isAtLeastNetherUpdate()) {
            editPacketAtLeastNetherUpdate(packet);
        } else {
            editPacketBeforeNetherUpdate(packet);
        }

    }

    /**
     * @return True if the running server is at least 1.16.
     */
    private boolean isAtLeastNetherUpdate() {

        Comparison result = ReflectionUtil.compareServerVersions(currentVersion, ServerVersion.V1_16);
        return result == Comparison.SAME || result == Comparison.MAJOR;

    }

    private boolean checkWorldAtLeastNetherUpdate(@NotNull PacketContainer packet) {

        World world = getWorld(null, packet);
        World.Environment environment = null;

        if (world != null) {
            environment = world.getEnvironment();
        }

        if (isDebugMode()) {

            String envName = null;

            if (environment != null) {
                envName = environment.name();
            }

            Logging.finest(ChatColor.AQUA + "Packet-world-env: %s", envName);

        }

        return environment == World.Environment.NORMAL;

    }

    private boolean checkWorldBeforeNetherUpdate(@NotNull PacketContainer packet) {

        int worldDimension = -1;

        /*
         * [Warning]
         *   The following commented piece of code is bugged!
         *
         *   1) The method getDimension() from WrapperPlayServerLogin class is incorrect because
         *      it gets the entity ID instead of the dimension number.
         *      Indicted line (1): worldDimension = wrapperPlayServerLogin.getDimension()
         *
         *      For this reason, according to the protocol 1.15.2 APIs (that you can find
         *      at: https://wiki.vg/index.php?title=Protocol&oldid=16067#Join_Game), the
         *      correct integer field index is 1 and not 0.
         *
         *   2) The method getDimension() from WrapperPlayServerRespawn class throws an error.
         *      Indicted line: worldDimension = wrapperPlayServerRespawn.getDimension()
         *
         * The following solution is for both packets.
         */
        @SuppressWarnings("deprecation") // the getDimensions() method will be removed from ProtocolLib.
        List<Integer> dimensionList = packet.getDimensions().getValues();
        Optional<Integer> optional = dimensionList.stream().findAny();
        boolean isPresent = optional.isPresent();

        if (isDebugMode()) {
            Logging.finest(ChatColor.AQUA + "packet-world-present: %s", String.valueOf(isPresent));
        }

        if (isPresent) {
            worldDimension = optional.get();
        }

        if (isDebugMode()) {
            Logging.finest(ChatColor.AQUA + "packet-world-dimension: %d", worldDimension);
        }

        return worldDimension == OVERWORLD_DIMENSION;

    }

    /**
     * Edit the login or respawn packet if the server version is at least 1.16.
     * @param packet The not-null instance of the packet container.
     */
    private void editPacketAtLeastNetherUpdate(@NotNull PacketContainer packet) {

        PacketType packetType = packet.getType();
        StructureModifier<Boolean> booleans = packet.getBooleans();

        if (isJoinGamePacketType(packetType)) {

            booleans.write(LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);

            if (isDebugMode()) {

                Logging.finest(ChatColor.AQUA + "Packet-field-index: %d",
                        LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX);
                Logging.finest(ChatColor.AQUA + "Packet-field-value: %b",
                        booleans.read(LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX));

            }

        } else if (isRespawnPacketType(packetType)) {

            booleans.write(RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);

            if (isDebugMode()) {

                Logging.finest(ChatColor.AQUA + "Packet-field-index: %d",
                        RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX);
                Logging.finest(ChatColor.AQUA + "Packet-field-value: %b",
                        booleans.read(RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX));

            }

        }

    }

    /**
     * Edit the login or respawn packet if the server version is before 1.16.
     * @param packet The not-null instance of the packet container.
     */
    private void editPacketBeforeNetherUpdate(@NotNull PacketContainer packet) {

        PacketType packetType = packet.getType();

        if (packetType == WrapperPlayServerLogin.TYPE) {

            WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
            wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

            if (isDebugMode()) {
                Logging.finest(ChatColor.AQUA + "Packet-level-type: %s",
                        wrapperPlayServerLogin.getLevelType().name());
            }

        } else if (packetType == WrapperPlayServerRespawn.TYPE) {

            WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
            wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

            if (isDebugMode()) {
                Logging.finest(ChatColor.AQUA + "Packet-level-type: %s",
                        wrapperPlayServerRespawn.getLevelType().name());
            }

        }

    }

    /**
     * @param packetType The not-null instance of the packet type.
     * @return True if the packet is a LOGIN (Join Game) packet.
     */
    private static boolean isJoinGamePacketType(@NotNull PacketType packetType) {
        return packetType == PacketType.Play.Server.LOGIN;
    }

    /**
     * @param packetType The not-null instance of the packet type.
     * @return True if the packet is a RESPAWN packet.
     */
    private static boolean isRespawnPacketType(@NotNull PacketType packetType) {
        return packetType == PacketType.Play.Server.RESPAWN;
    }

}
