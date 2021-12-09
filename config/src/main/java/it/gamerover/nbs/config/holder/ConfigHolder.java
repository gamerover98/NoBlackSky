package it.gamerover.nbs.config.holder;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BooleanProperty;
import ch.jalu.configme.properties.StringSetProperty;

/**
 * @author gamerover98
 * Simple plugin configuration class.
 */
public class ConfigHolder implements SettingsHolder {

    private static final String DEBUG_MODE_PROPERTY_NAME     = "debug-mode";
    private static final String ALWAYS_ENABLED_PROPERTY_NAME = "always-enabled";
    private static final String WORLDS_PROPERTY_NAME         = "worlds";

    private static final String DEFAULT_EXAMPLE_WORLD = "exampleWorld";

    @Comment({
            "By enabling this property, debug strings will be logged",
            "By default, this value is false."
    })
    public static final BooleanProperty DEBUG_MODE =
            new BooleanProperty(DEBUG_MODE_PROPERTY_NAME, false);

    @Comment({
            "By enabling this property, the black sky glitch in all your NORMAL worlds",
            "(not nether or end worlds) will be fixed."
    })
    public static final BooleanProperty ALWAYS_ENABLED =
            new BooleanProperty(ALWAYS_ENABLED_PROPERTY_NAME, false);

    @Comment({
            "If " + ALWAYS_ENABLED_PROPERTY_NAME + " property is disabled and you want to fix",
            "the black sky glitch in your world, add it into the list below.",
            "There's no need to add your ParadiseLand worlds, it will be automatically solved."
    })
    public static final StringSetProperty WORLDS =
            new StringSetProperty(WORLDS_PROPERTY_NAME, DEFAULT_EXAMPLE_WORLD);

    private ConfigHolder() {
        // nothing to do
    }

}