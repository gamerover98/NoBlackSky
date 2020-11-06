package it.gamerover.nbs.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;

import it.gamerover.nbs.NoBlackSky;
import lombok.Getter;

/**
 * 
 * @author gamerover98
 *
 */
public class NBS_Configuration {

	private FileManager configurations;
	
	/*
	 * Blacklisted worlds
	 */
	@Getter private List<String> blacklist;
	
	/*
	 * paradise_land option
	 */
	@Getter private boolean paradise_land;

	private static final String BLACKLIST_PATH = "blacklist";
	private static final String PARADISELAND_PATH = "paradise_land";

	public NBS_Configuration(NoBlackSky instance) throws IOException, InvalidConfigurationException {

		this.configurations = new FileManager("config.yml", "/config.yml");
		this.blacklist = new ArrayList<String>();
		this.paradise_land = false;
		
		this.reload();

	}

	/*
	 * Add a world to the blacklist
	 */
	public boolean add_blacklist_world(String world_name) throws IOException {

		if (this.contains_blacklist_world(world_name)) {
			return false;
		}

		this.blacklist.add(world_name);

		this.configurations.set(BLACKLIST_PATH, this.blacklist);
		this.configurations.save();

		return true;

	}

	/*
	 * Remove a world from the blacklist
	 */
	public boolean remove_blacklist_world(String world_name) throws IOException {

		if (!this.contains_blacklist_world(world_name)) {
			return false;
		}

		this.blacklist.remove(world_name);

		this.configurations.set(world_name, this.blacklist);
		this.configurations.save();

		return true;

	}

	/*
	 * Check if blacklist contains world_name (method argument)
	 */
	public boolean contains_blacklist_world(String world_name) {
		return this.blacklist.contains(world_name);
	}

	/*
	 * Load or reload the config.yml
	 */
	public void reload() throws IOException, InvalidConfigurationException {

		this.blacklist.clear();
		this.configurations.load();

		this.blacklist = this.configurations.getStringList(BLACKLIST_PATH);
		this.paradise_land = this.configurations.getBoolean(PARADISELAND_PATH);

	}

}
