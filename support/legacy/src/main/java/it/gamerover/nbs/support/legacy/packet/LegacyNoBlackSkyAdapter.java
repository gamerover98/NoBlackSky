package it.gamerover.nbs.support.legacy.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LegacyNoBlackSkyAdapter extends NoBlackSkyAdapter {

    /**
     * This world type will fix the black sky under height 61.
     * Wiki at: https://wiki.vg/Protocol#Join_Game
     */
    private static final WorldType FLAT_WORLD_TYPE = WorldType.FLAT;

    public LegacyNoBlackSkyAdapter(@NotNull Plugin plugin, @NotNull ServerVersion currentVersion) {
        super(plugin, currentVersion);
    }

    @Override
    protected boolean isOverworld(@NotNull World world) {

        World.Environment environment = world.getEnvironment();
        return environment == World.Environment.NORMAL;

    }

    @Override
    protected void editPacket(@NotNull PacketContainer packet, @NotNull PacketType packetType) {

        if (packetType == WrapperPlayServerLogin.TYPE) {

            WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
            wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

        } else if (packetType == WrapperPlayServerRespawn.TYPE) {

            WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
            wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

        }

    }

}
