package it.gamerover.nbs.core.packet;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.events.ListenerPriority;
import it.gamerover.nbs.config.ConfigManager;
import it.gamerover.nbs.reflection.ServerVersion;
import lombok.Getter;
import org.bukkit.World;
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
public abstract class NoBlackSkyAdapter extends PacketAdapter {

	/**
	 * ParadiseLand's package to auto-manage its worlds.
	 * https://www.spigotmc.org/resources/paradise-land-1-8-8-1-16-x-skyworld-generator.28056/
	 */
	private static final String PARADISE_LAND_PACKAGE = "it.gamerover.paradise";

	/**
	 * Gets the not null current server version enum instance.
	 */
	@Getter @NotNull
	protected final ServerVersion currentVersion;

	protected NoBlackSkyAdapter(@NotNull Plugin plugin, @NotNull ServerVersion currentVersion) {

		super(new AdapterParameteters()
				.plugin(plugin)
				.types(WrapperPlayServerLogin.TYPE, WrapperPlayServerRespawn.TYPE)
				.listenerPriority(ListenerPriority.HIGHEST));

		this.currentVersion = currentVersion;

	}

	/**
	 * Check if the World's environment is overworld.
	 *
	 * @param world The not-null player world instance.
	 * @return True if the world is an overworld.
	 */
	protected abstract boolean isOverworld(@NotNull World world);

	/**
	 * Edit the current packet with the no black sky fix.
	 *
	 * @param packet     The not-null instance of the packet container.
	 * @param packetType The not-null instance of the packet type.
	 */
	protected abstract void editPacket(@NotNull PacketContainer packet,
									   @NotNull PacketType packetType);

	/**
	 * Edit the world type of world when the
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

		if (!isOverworld(world)) {
			return;
		}

		String worldName = world.getName();
		boolean alwaysEnabled = ConfigManager.isAlwaysEnabled();
		boolean isParadiseWorld = isParadiseLandWorld(world);

		Set<String> worlds = ConfigManager.getWorlds();

		if (alwaysEnabled || isParadiseWorld || worlds.contains(worldName)) {

			PacketContainer packet = event.getPacket();
			editPacket(packet, packetType);

		}

	}

	private boolean isParadiseLandWorld(@NotNull World world) {

		ChunkGenerator chunkGenerator = world.getGenerator();

		if (chunkGenerator == null) {
			return false;
		}

		String className = chunkGenerator.getClass().getName();
		return className.startsWith(PARADISE_LAND_PACKAGE);

	}

}
