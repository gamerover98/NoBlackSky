package it.gamerover.nbs;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import it.gamerover.nbs.command.NBS_Command;
import it.gamerover.nbs.configuration.NBS_Configuration;
import it.gamerover.nbs.logger.NBS_Logger;
import lombok.Getter;

/**
 * 
 * @author gamerover98
 *
 *	This, like all the classes of the project WorldTypeChanger
 *	is free and it can be used to do more
 *
 */
public class NBS extends JavaPlugin {

	/*
	 * NBS object instance
	 */
	@Getter private static NBS instance;

	/*
	 * Main config instance
	 */
	@Getter private static NBS_Configuration configuration;

	/*
	 * PacketAdapter instance
	 */
	@Getter private static NBS_PacketAdapter nbsPacketAdapter;

	/*
	 * ProtocolManager instance of ProtocolLib
	 */
	@Getter private static ProtocolManager protocolManager;

	private NBS_PacketAdapter nbs_PacketAdapter;
	private boolean enable = true;

	@Override
	public void onLoad() {

		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();

		NBS_Logger.init();
		
		try {

			//Start config.yml
			configuration = new NBS_Configuration(instance);

		} catch (IOException | InvalidConfigurationException ex) {

			this.enable = false;
			NBS_Logger.error(ex, "An error occurred reading config.yml");

		}


	}

	@Override
	public void onEnable() {

		if (!this.enable) {

			this.disable();
			return;

		}

		//The NoBlackSky command
		PluginCommand nbsCommand = getInstance().getCommand("NoBlackSky");

		nbsCommand.setExecutor(new NBS_Command());
		nbsCommand.setAliases(Arrays.asList(new String[]{"nbs"}));

		this.nbs_PacketAdapter = new NBS_PacketAdapter(instance);
		
		protocolManager.addPacketListener(nbs_PacketAdapter);
		NBS_Logger.info("NoBlackSky successfully enabled!");
		
	}

	@Override
	public void onDisable() {
		protocolManager.removePacketListener(this.nbs_PacketAdapter);
	}
	
	/*
	 * Disable plugin
	 */
	private void disable() {

		NBS_Logger.info("An error occurred, ##### Disabling NBS #####");
		super.setEnabled(false);

	}

}
