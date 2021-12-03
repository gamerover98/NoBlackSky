package it.gamerover.nbs.core.configuration;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import it.gamerover.nbs.core.NoBlackSky;
import it.gamerover.nbs.core.configuration.holder.ConfigHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
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
     * @return True if all normal worlds must be fixed by default
     * @throws IllegalStateException If the configuration is not loaded.
     */
    public static boolean isAlwaysEnabled() {

        checkSettings();

        assert settingsManager != null;
        return settingsManager.getProperty(ConfigHolder.ALWAYS_ENABLED);

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
     * @param worldName The world name.
     * @return True if the world name is already contained into the config list.
     */
    public static boolean containsWorld(@NotNull String worldName) {
        return getWorlds().stream().anyMatch(name -> name.equals(worldName));
    }

    /**
     * @param worldName The world name.
     * @return True if the world name is added into the config file,
     *         False if it is already contained.
     */
    public static boolean addWorld(@NotNull String worldName) {

        if (worldName.isEmpty()) {
            throw new IllegalArgumentException("The world name argument cannot be empty");
        }

        if (containsWorld(worldName)) {
            return false;
        }

        assert settingsManager != null;
        Set<String> worlds = new HashSet<>(getWorlds());
        worlds.add(worldName);

        settingsManager.setProperty(ConfigHolder.WORLDS, worlds);
        settingsManager.save();

        return true;

    }

    /**
     * @param worldName The world name.
     * @return True if the world name is removed from the config file,
     *         False if isn't contained.
     */
    public static boolean removeWorld(@NotNull String worldName) {

        if (worldName.isEmpty()) {
            throw new IllegalArgumentException("The world name argument cannot be empty");
        }

        if (!containsWorld(worldName)) {
            return false;
        }

        assert settingsManager != null;
        Set<String> worlds = new HashSet<>(getWorlds());
        worlds.remove(worldName);

        settingsManager.setProperty(ConfigHolder.WORLDS, worlds);
        settingsManager.save();
        return true;

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
