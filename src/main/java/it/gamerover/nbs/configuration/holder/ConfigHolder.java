package it.gamerover.nbs.configuration.holder;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BooleanProperty;
import it.gamerover.nbs.configuration.property.StringSetProperty;

/**
 * @author gamerover98
 * Simple plugin configuration class.
 */
public class ConfigHolder implements SettingsHolder {

	private static final String ALWAYS_ENABLED_PROPERTY_NAME = "always-enabled";

	private static final String WORLDS_PROPERTY_NAME = "worlds";

	@Comment({
			"By enabling this property, the black sky glitch in all your NORMAL worlds",
			"(not nether or end worlds) will be fixed"
	})
	public static final BooleanProperty ALWAYS_ENABLED =
			new BooleanProperty(ALWAYS_ENABLED_PROPERTY_NAME, false);

	@Comment({
			"If " + ALWAYS_ENABLED_PROPERTY_NAME + " property is disabled and you want to fix",
			"the black sky glitch in your world, add it into the list below.",
			"There's not need to add your ParadiseLand worlds, it will be automatically solved."
	})
	public static final StringSetProperty WORLDS =
			new StringSetProperty(WORLDS_PROPERTY_NAME, "exampleWorld");

	private ConfigHolder() {
		// noting to do
	}

}
