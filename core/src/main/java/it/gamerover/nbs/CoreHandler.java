package it.gamerover.nbs;

import com.dumptruckman.minecraft.util.Logging;
import com.google.common.base.Joiner;
import it.gamerover.nbs.config.ConfigManager;
import it.gamerover.nbs.config.holder.ConfigHolder;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ReflectionContainer;
import it.gamerover.nbs.reflection.ReflectionException;
import it.gamerover.nbs.reflection.ServerVersion;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import it.gamerover.nbs.core.command.PluginCommand;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.command.CommandController;

@SuppressWarnings("squid:S2696")
public abstract class CoreHandler {

	/**
	 * Gets the CoreHandler instance.
	 */
	@Getter
	private static CoreHandler instance;

	/**
	 * Gets the current server version.
	 * <p>
	 *     If the server version is not supported, it will be the latest
	 *     available version supported by the plugin.
	 * </p>
	 */
	@Getter
	private static ServerVersion serverVersion;

	/**
	 * Gets the reflection container instance.
	 */
	@Getter
	private static ReflectionContainer reflectionContainer;

	/**
	 * NoBlackSky plugin instance.
	 */
	@Getter @NotNull
	private final Plugin plugin;

	/**
	 * Gets the ProtocolManager instance of ProtocolLib.
	 */
	@Getter
	private ProtocolManager protocolManager;

	/**
	 * Gets the command controller instance.
	 */
	@Getter
	private CommandController commandController;

	/**
	 * Gets the main plugin command.
	 */
	@Getter
	private PluginCommand pluginCommand;

	@SuppressWarnings("squid:S3010") // SonarLint: Remove this assignment of "instance".
	protected CoreHandler(@NotNull Plugin plugin) {

		this.plugin = plugin;
		CoreHandler.instance = this;

	}

	/**
	 * @return The PacketAdapter that edits the login/respawn player
	 *         packets to fix the black sky glitch.
	 */
	@NotNull
	protected abstract NoBlackSkyAdapter getNoBlackSkyAdapter();

	/**
	 * Invoked when the plugin is loading.
	 */
	protected void pluginLoading() {

		this.protocolManager = ProtocolLibrary.getProtocolManager();
		ConfigManager.reload(plugin);

		if (ConfigManager.isDebugMode()) {

			Logging.setDebugLevel(3); // 3 for finest logs.
			Logging.fine(ChatColor.YELLOW + "Debug-mode is enabled!");
			Logging.fine(ChatColor.YELLOW + "Current running supported server version: %s",
					CoreHandler.getServerVersion().getVersion());
			Logging.fine(ChatColor.YELLOW + "Config: %s property: %s", ConfigHolder.ALWAYS_ENABLED_PROPERTY_NAME,
					String.valueOf(ConfigManager.isAlwaysEnabled()));
			Logging.fine(ChatColor.YELLOW + "Config: %s property: {%s}", ConfigHolder.WORLDS_PROPERTY_NAME,
					Joiner.on(", ").join(ConfigManager.getWorlds()));

		}

	}

	/**
	 * Invoked when the plugin is enabling.
	 */
	protected void pluginEnabling() {

		if (protocolManager == null) {
			return;
		}

		protocolManager.addPacketListener(getNoBlackSkyAdapter());

		commandController = new CommandController((JavaPlugin) plugin);
		pluginCommand     = new PluginCommand(commandController);

		Logging.finest("%s successful enabled!", plugin.getName());

	}

	/**
	 * Invoked when the plugin is disabling.
	 */
	protected void pluginDisabling() {

		if (protocolManager == null) {
			return;
		}

		protocolManager.removePacketListeners(plugin);

		if (commandController != null && pluginCommand != null) {

			try {

				commandController.removeCommand(pluginCommand);

			} catch (IllegalStateException isEx) {
				// nothing to do.
			}

		}

		Logging.finest(ChatColor.GREEN + "%s successful disabled!", plugin.getName());
		Logging.shutdown();

	}

	/**
	 * Initialize the CoreHandler by loading static stuff like reflections and server version.
	 * @throws ReflectionException Thrown due to a reflection's error.
	 */
	static void init() throws ReflectionException {

		if (reflectionContainer == null) {
			reflectionContainer = new ReflectionContainer();
		}

		ServerVersion runningVersion = ServerVersion.getRunningServerVersion(reflectionContainer);

		// The server running with an unknown server version.
		if (runningVersion == null) {

			ServerVersion latestVersion = ServerVersion.getLatest(false);
			String message = "Cannot find the current server version, "
					+ "attempting to start the plugin with the latest ("
					+ latestVersion.getVersion() + ") supported version ...";

			Logging.warning(ChatColor.YELLOW + message);
			runningVersion = latestVersion;

		} else {

			String message = "Detected " + runningVersion.getVersion() + " server version";
			Logging.info(ChatColor.GREEN + message);

		}

		serverVersion = runningVersion;

	}

}
