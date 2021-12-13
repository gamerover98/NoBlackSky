package it.gamerover.nbs.config.holder;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BooleanProperty;
import ch.jalu.configme.properties.StringSetProperty;

import static it.gamerover.nbs.util.YamlUtil.createProperty;

/**
 * @author gamerover98
 * Simple plugin configuration class.
 */
public class ConfigHolder implements SettingsHolder {

    public static final String DEBUG_MODE_PROPERTY_NAME       = createProperty("debug", "mode");
    public static final String ALWAYS_ENABLED_PROPERTY_NAME   = createProperty("always", "enabled");
    public static final String INCLUDE_C_WORLDS_PROPERTY_NAME = createProperty("include", "custom", "worlds");
    public static final String WORLDS_PROPERTY_NAME           = createProperty("worlds");

    private static final String DEFAULT_EXAMPLE_WORLD = "exampleWorld";

    @Comment({
            "By enabling this property, a debug.log file will be created into the plugins/NoBlackSky folder.",
            "Debug logs will be printed on the server console and appended on this debug file.",
            "This file is required when a bug or an issue is reported. By default, this value is false."
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
            "From Spigot 1.17, you can create custom worlds so, if you want to",
            "include it, enable this property. Obviously, by enabling this property",
            "from a version before 1.17, this will be automatically reset to false."
    })
    public static final BooleanProperty INCLUDE_CUSTOM_WORLDS =
            new BooleanProperty(INCLUDE_C_WORLDS_PROPERTY_NAME, false);

    @Comment({
            "If always-enabled property is disabled and you want to fix",
            "the black sky glitch in your world, add it into the list below.",
            "There's no need to add your ParadiseLand worlds, it will be automatically solved."
    })
    public static final StringSetProperty WORLDS =
            new StringSetProperty(WORLDS_PROPERTY_NAME, DEFAULT_EXAMPLE_WORLD);

    private ConfigHolder() {
        // nothing to do
    }

}