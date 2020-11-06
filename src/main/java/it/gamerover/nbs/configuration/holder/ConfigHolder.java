package it.gamerover.nbs.configuration.holder;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import it.gamerover.nbs.configuration.property.StringSetProperty;

/**
 * @author gamerover98
 * Simple plugin configuration class.
 */
public class ConfigHolder implements SettingsHolder {

	private static final String WORLDS_PROPERTY_NAME = "worlds";

	@Comment({
			"If you want to fix the black sky glitch in your world, add it into the list below.",
			"There's not need to add your ParadiseLand worlds, it will be automatically solved."
	})
	public static final StringSetProperty WORLDS =
			new StringSetProperty(WORLDS_PROPERTY_NAME, "exampleWorld");

	private ConfigHolder() {
		// noting to do
	}

}
