package it.gamerover.nbs.configuration;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import it.gamerover.nbs.NoBlackSky;
import it.gamerover.nbs.configuration.holder.ConfigHolder;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * @author gamerover98
 * Manage the plugin configuration.
 */
public class ConfigManager {

    /**
     * The config file name.
     * It will be located in the /plugins/NoBlackSky folder.
     */
    private static final String CONFIG_FILE_NAME = "config.yml";

    /**
     * The ConfigMe settings manager instance.
     */
    @Nullable
    private static SettingsManager settingsManager;

    private ConfigManager() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * Reload or load the configuration file.
     */
    public static void reload() {

        NoBlackSky plugin = NoBlackSky.getInstance();

        if (settingsManager == null) {

            File dataFolder = plugin.getDataFolder();
            File configFile = new File(dataFolder, CONFIG_FILE_NAME);

            settingsManager = SettingsManagerBuilder
                    .withYamlFile(configFile)
                    .configurationData(ConfigHolder.class)
                    .useDefaultMigrationService()
                    .create();

        } else {
            settingsManager.reload();
        }

    }

    /**
     * @return An unmodifiable set of strings that contains the worlds' name.
     * @throws IllegalStateException If the configuration is not loaded.
     */
    public static Set<String> getWorlds() {

        checkSettings();

        assert settingsManager != null;
        return Collections.unmodifiableSet(settingsManager.getProperty(ConfigHolder.WORLDS));

    }

    /**
     * @throws IllegalStateException If the settings manager instance is null,
     *                               so if the configuration is not loaded.
     */
    private static void checkSettings() {

        if (settingsManager != null) {
            return;
        }

        throw new IllegalStateException("the config " + CONFIG_FILE_NAME + " is not loaded");

    }

}
