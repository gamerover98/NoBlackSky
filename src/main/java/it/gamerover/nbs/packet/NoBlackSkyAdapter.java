package it.gamerover.nbs.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.reflect.StructureModifier;
import it.gamerover.nbs.NoBlackSky;
import it.gamerover.nbs.configuration.ConfigManager;
import it.gamerover.nbs.util.GenericUtil;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
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
	 * ParadiseLand's package to auto-manage its worlds.
	 * https://www.spigotmc.org/resources/paradise-land-1-8-8-1-16-x-skyworld-generator.28056/
	 */
	private static final String PARADISE_LAND_PACKAGE = "it.gamerover.paradise";

	/**
	 * Since 1.16 the packets of Join Game and Respawn were edited.
	 * This string helps to edit correctly these packets.
	 */
	private static final String V1_16 = "1.16";

	/**
	 * This world type will fix the black sky under height 61.
	 * Wiki at: https://wiki.vg/Protocol#Join_Game
	 */
	private static final WorldType FLAT_WORLD_TYPE = WorldType.FLAT;

	/**
	 * isFlat field on the 1.16+ packet is at index 4.
	 * Wiki at: https://wiki.vg/Protocol#Respawn
	 */
	private static final int LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX = 4;

	/**
	 * isFlat field on the 1.16+ packet is at index 4.
	 */
	private static final int RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX = 1;

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

		boolean alwaysEnabled = ConfigManager.isAlwaysEnabled();
		boolean isParadiseWorld = isParadiseLandWorld(world);

		PacketContainer packet = event.getPacket();
		Set<String> worlds = ConfigManager.getWorlds();

		if (alwaysEnabled || isParadiseWorld || worlds.contains(world.getName())) {

			GenericUtil.Comparison comparison = GenericUtil
					.compareServerVersions(serverVersion, V1_16);

			if (comparison == GenericUtil.Comparison.MINOR) {
				beforeNetherUpdate(packetType, packet);
			} else {
				afterNetherUpdate(packetType, packet);
			}

		}

	}

	private boolean isParadiseLandWorld(@NotNull World world) {

		ChunkGenerator chunkGenerator = world.getGenerator();
		String className = chunkGenerator.getClass().getName();

		return className.startsWith(PARADISE_LAND_PACKAGE);

	}

	private void afterNetherUpdate(@NotNull PacketType packetType, @NotNull PacketContainer packet) {

		StructureModifier<Boolean> booleans = packet.getBooleans();

		if (packetType == PacketType.Play.Server.LOGIN) {
			booleans.write(LOGIN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
		} else if (packetType == PacketType.Play.Server.RESPAWN) {
			booleans.write(RESPAWN_IS_FLAT_BOOLEAN_FIELD_INDEX, true);
		}

	}

	private void beforeNetherUpdate(@NotNull PacketType packetType, @NotNull PacketContainer packet) {

		if (packetType == WrapperPlayServerLogin.TYPE) {

			WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
			wrapperPlayServerLogin.setLevelType(FLAT_WORLD_TYPE);

		} else if (packetType == WrapperPlayServerRespawn.TYPE) {

			WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
			wrapperPlayServerRespawn.setLevelType(FLAT_WORLD_TYPE);

		}

	}

}
