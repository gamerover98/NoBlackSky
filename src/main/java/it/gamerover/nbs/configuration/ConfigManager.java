package it.gamerover.nbs.configuration;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import it.gamerover.nbs.NoBlackSky;
import it.gamerover.nbs.configuration.holder.ConfigHolder;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public class ConfigManager {

    private static final String CONFIG_FILE_NAME = "config.yml";

    @Nullable
    private static SettingsManager settingsManager;

    private ConfigManager() {
        throw new IllegalStateException("This is a static class");
    }

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

    public static Set<String> getWorlds() {

        checkSettings();

        assert settingsManager != null;
        return settingsManager.getProperty(ConfigHolder.WORLDS);

    }

    private static void checkSettings() {

        if (settingsManager == null) {
            throw new IllegalStateException("the config " + CONFIG_FILE_NAME + " is not loaded");
        }

    }

}
