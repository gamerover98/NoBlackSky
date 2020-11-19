package it.gamerover.nbs.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import it.gamerover.nbs.NoBlackSky;
import it.gamerover.nbs.configuration.ConfigManager;
import it.gamerover.nbs.util.GenericUtil;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author gamerover98
 */
public class NoBlackSkyAdapter extends PacketAdapter {

	/**
	 * Since 1.16 the packets of PlayLogin and Respawn were edited.
	 * This string helps to edit correctly these packets.
	 */
	private static final String V1_16 = "1.16";

	/**
	 * This world type will fix the black sky under height 61.
	 */
	private static final WorldType FLAT_WORLD_TYPE = WorldType.FLAT;

	public NoBlackSkyAdapter(Plugin plugin) {
		super(plugin, WrapperPlayServerLogin.TYPE, WrapperPlayServerRespawn.TYPE);
	}

	/**
	 * Edit the world type of a world when the
	 * player join into the server, respawn or change world.
	 */
	@Override
	public void onPacketSending(PacketEvent event) {

		PacketType packetType = event.getPacketType();
		Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		World world = player.getWorld();
		World.Environment environment = world.getEnvironment();

		if (environment != World.Environment.NORMAL) {
			return;
		}

		String serverVersion = NoBlackSky.getReflectionContainer()
				.getMinecraft().getMinecraftServer().getVersion();

		PacketContainer packet = event.getPacket();
		boolean alwaysEnabled = ConfigManager.isAlwaysEnabled();
		Set<String> worlds = ConfigManager.getWorlds();

		if (alwaysEnabled || worlds.contains(world.getName())) {

			GenericUtil.Comparison comparison = GenericUtil.compareServerVersions(serverVersion, V1_16);

			if (comparison == GenericUtil.Comparison.MINOR) {
				beforeNetherUpdate(packetType, packet);
			} else {
				afterNetherUpdate(packetType, packet);
			}

		}

	}

	private void afterNetherUpdate(@NotNull PacketType packetType, @NotNull PacketContainer packet) {

		getPlugin().getLogger().info("after");
		//TODO implementation

	}

	private void beforeNetherUpdate(@NotNull PacketType packetType, @NotNull PacketContainer packet) {

		getPlugin().getLogger().info("before");
		//TODO remove the above test print

		if (packetType == WrapperPlayServerLogin.TYPE) {

			WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
			wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

		} else if (packetType == WrapperPlayServerRespawn.TYPE) {

			WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
			wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

		}

	}

}
