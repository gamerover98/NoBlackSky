package it.gamerover.nbs;

import java.util.Arrays;

import it.gamerover.nbs.configuration.ConfigManager;
import it.gamerover.nbs.packet.NoBlackSkyAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import it.gamerover.nbs.command.PluginCommand;
import it.gamerover.nbs.logger.PluginLogger;
import lombok.Getter;

/**
 * 
 * @author gamerover98
 *
 *	This, like all the classes of the project WorldTypeChanger
 *	is free and it can be used to do more
 *
 */
public class NoBlackSky extends JavaPlugin {

	/**
	 * Gets the NBS instance.
	 */
	@Getter
	private static NoBlackSky instance;

	/*
	 * PacketAdapter instance
	 */
	@Getter
	private static NoBlackSkyAdapter nbsPacketAdapter;

	/*
	 * ProtocolManager instance of ProtocolLib
	 */
	@Getter
	private static ProtocolManager protocolManager;

	private NoBlackSkyAdapter noBlackSkyAdapter;
	private boolean enable = true;

	@Override
	public void onLoad() {

		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();

		PluginLogger.init();

		try {

			ConfigManager.reload();

		} catch (Exception ex) {

			this.enable = false;
			PluginLogger.error("An error has occurred while reading config.yml", ex);

		}

	}

	@Override
	public void onEnable() {

		if (!this.enable) {

			this.disable();
			return;

		}

		//The NoBlackSky command
		org.bukkit.command.PluginCommand nbsCommand = getInstance().getCommand("NoBlackSky");

		nbsCommand.setExecutor(new PluginCommand());
		nbsCommand.setAliases(Arrays.asList(new String[]{"nbs"}));

		this.noBlackSkyAdapter = new NoBlackSkyAdapter(instance);
		
		protocolManager.addPacketListener(noBlackSkyAdapter);
		PluginLogger.info("NoBlackSky successfully enabled!");
		
	}

	@Override
	public void onDisable() {
		protocolManager.removePacketListener(this.noBlackSkyAdapter);
	}
	
	/*
	 * Disable plugin
	 */
	private void disable() {

		PluginLogger.info("An error occurred, ##### Disabling NBS #####");
		super.setEnabled(false);

	}

}
