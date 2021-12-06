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

import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * @param packet The not-null instance of the packet container.
	 * @return True if the world dimension sent with this packet is an overworld.
	 */
	protected abstract boolean isOverworld(@NotNull PacketContainer packet);

	/**
	 * Gets the world from the player or the packet.
	 * <p>
	 *     World from the packet is available from 1.16+.
	 * </p>
	 *
	 * @param player The nullable instance of the player.
	 * @param packet The not-null instance of the packet container.
	 * @return The world instance, it can be null!
	 */
	@Nullable
	protected abstract World getWorld(@Nullable Player player, @NotNull PacketContainer packet);

	/**
	 * Edit the current packet with the no black sky fix.
	 * @param packet The not-null instance of the packet container.
	 */
	protected abstract void editPacket(@NotNull PacketContainer packet);

	/**
	 * Edit the world type of world when the
	 * player join into the server, respawn or change world.
	 */
	@Override
	public void onPacketSending(PacketEvent event) {

		Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		PacketContainer packet = event.getPacket();

		if (!isOverworld(packet)) {
			return;
		}

		boolean alwaysEnabled = ConfigManager.isAlwaysEnabled();

		if (!alwaysEnabled) {

			World world = getWorld(player, packet);

			if (world == null) {
				return;
			}

			Set<String> worlds = ConfigManager.getWorlds();
			boolean isParadiseWorld = isParadiseLandWorld(world);

			if (!isParadiseWorld && !worlds.contains(world.getName())) {
				return;
			}

		}

		editPacket(packet);

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
