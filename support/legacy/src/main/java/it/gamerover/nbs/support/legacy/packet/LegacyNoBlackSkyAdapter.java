package it.gamerover.nbs.support.legacy.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LegacyNoBlackSkyAdapter extends NoBlackSkyAdapter {

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

    public LegacyNoBlackSkyAdapter(@NotNull Plugin plugin, @NotNull ServerVersion currentVersion) {
        super(plugin, currentVersion);
    }

    @Override
    protected boolean isOverworld(@NotNull PacketContainer packet) {

        PacketType packetType = packet.getType();
        int worldDimension = -1;

        if (packetType == WrapperPlayServerLogin.TYPE) {

            WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);

            /*
             * [Warning]
             *   The following commented piece of code is bugged!
             *   The method getDimension() from WrapperPlayServerLogin class is incorrect because
             *   it gets the entity ID instead of the dimension number.
             *
             * indicted line: worldDimension = wrapperPlayServerLogin.getDimension()
             *
             * For this reason, according to the protocol 1.12.2 APIs (that you can find
             * at: https://wiki.vg/index.php?title=Protocol&oldid=14204#Join_Game), the
             * correct integer field index is 1 and not 0.
             */
            worldDimension = wrapperPlayServerLogin.getHandle().getIntegers().read(1);

        } else if (packetType == WrapperPlayServerRespawn.TYPE) {

            WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
            worldDimension = wrapperPlayServerRespawn.getDimension();

        }

        return worldDimension == OVERWORLD_DIMENSION;

    }

    @Nullable
    @Override
    protected World getWorld(@Nullable Player player, @NotNull PacketContainer packet) {

        if (player == null) {
            return null;
        }

        /*
         * Trying to fix the exception "The method getWorld is not supported for temporary players":
         * https://github.com/gamerover98/NoBlackSky/issues/4
         */
        try {

            return player.getWorld();

        } catch (Exception ex) {
            return null; // if it fails, the black sky fix doesn't work for the current logging player.
        }

    }

    @Override
    protected void editPacket(@NotNull PacketContainer packet) {

        PacketType packetType = packet.getType();

        if (packetType == WrapperPlayServerLogin.TYPE) {

            WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
            wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

        } else if (packetType == WrapperPlayServerRespawn.TYPE) {

            WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
            wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

        }

    }

}
