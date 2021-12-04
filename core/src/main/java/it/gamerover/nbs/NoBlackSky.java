package it.gamerover.nbs;

import it.gamerover.nbs.config.ConfigManager;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ReflectionContainer;
import it.gamerover.nbs.reflection.ReflectionException;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import it.gamerover.nbs.core.command.PluginCommand;
import it.gamerover.nbs.core.logger.PluginLogger;
import lombok.Getter;
import xyz.tozymc.spigot.api.command.CommandController;

/**
 * 
 * @author gamerover98
 *
 * The main class of the project.
 */
@SuppressWarnings("squid:S2696")
public class NoBlackSky extends JavaPlugin {

	/**
	 * Gets the NBS instance.
	 */
	@Getter
	private static NoBlackSky instance;

	/**
	 * Gets the reflection container instance.
	 */
	@Getter
	private static ReflectionContainer reflectionContainer;

	/**
	 * Gets the ProtocolManager instance of ProtocolLib.
	 */
	@Getter
	private static ProtocolManager protocolManager;

	/**
	 * The PacketAdapter that edits the login and respawn player packet
	 * to fix the black sky glitch.
	 */
	@Getter
	private NoBlackSkyAdapter noBlackSkyAdapter;

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

	/**
	 * If true, the plugin will execute the onEnable method.
	 */
	private boolean isPluginStartable = true;

	@Override
	public void onLoad() {

		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();

		PluginLogger.init();

		try {

			reflectionContainer = new ReflectionContainer();
			ConfigManager.reload(this);

		} catch (ReflectionException reflectionException) {

			PluginLogger.error("A reflection error has occurred while loading", reflectionException);
			this.isPluginStartable = false;

		} catch (Exception ex) {

			PluginLogger.error("An error has occurred while reading config.yml", ex);
			this.isPluginStartable = false;

		}

	}

	@Override
	public void onEnable() {

		if (!isPluginStartable) {

			disablePlugin();
			return;

		}

		commandController = new CommandController(this);
		pluginCommand = new PluginCommand(commandController);

		this.noBlackSkyAdapter = new NoBlackSkyAdapter(instance);
		
		protocolManager.addPacketListener(noBlackSkyAdapter);
		PluginLogger.info("NoBlackSky successfully enabled!");
		
	}

	@Override
	public void onDisable() {
		protocolManager.removePacketListener(this.noBlackSkyAdapter);
	}

	/**
	 * Disable plugin with a warning message.
	 */
	private void disablePlugin() {

		PluginLogger.warning("Disabling NoBlackSky " + super.getDescription().getVersion());
		super.setEnabled(false);

	}

}
