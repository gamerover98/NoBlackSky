package it.gamerover.nbs;

import it.gamerover.nbs.configuration.ConfigManager;
import it.gamerover.nbs.packet.NoBlackSkyAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import it.gamerover.nbs.command.PluginCommand;
import it.gamerover.nbs.logger.PluginLogger;
import lombok.Getter;
import xyz.tozymc.spigot.api.command.CommandController;

/**
 * 
 * @author gamerover98
 *
 *	This, like all the classes of the project WorldTypeChanger
 *	is free and it can be used to do more
 *
 */
@SuppressWarnings("squid:S2696")
public class NoBlackSky extends JavaPlugin {

	/**
	 * Gets the NBS instance.
	 */
	@Getter
	private static NoBlackSky instance;

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

			ConfigManager.reload();

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
