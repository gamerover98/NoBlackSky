package it.gamerover.nbs.util;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Locale;

public final class YamlUtil {

    private static final String DOT  = ".";
    private static final String DASH = "-";

    /**
     * The YAML file extension without the dot.
     */
    public static final String YAML_FILE_EXTENSION = "yml";

    private YamlUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * Create a chain of words as path.
     *
     * For example: parent:   a.b.c
     *              property: d
     *              result:   a.b.c.d
     *
     * @param parent The not null parent string property.
     * @param property The not null string property.
     * @return The chained path: parent.property
     */
    public static String createPath(@NotNull String parent, @NotNull String property) {

        if (parent.isEmpty() || property.isEmpty()) {
            throw new IllegalArgumentException("The arguments cannot be empty");
        }

        return parent + DOT + property;

    }

    /**
     * Create a chain of words as property.
     *
     * For example: components: {"a", "b", "c", "d"}
     *              result:     a-b-c-d
     *
     * @param components The not null array of words.
     * @return component(0)-component(1)-component(2)-...-component(n-1)
     * @throws IllegalArgumentException If the components array is empty.
     */
    @NotNull
    public static String createProperty(@NotNull String... components) {

        if (components.length == 0) {
            throw new IllegalArgumentException("The argument array must be filled");
        }

        if (components.length == 1) {
            return components[0];
        }

        StringBuilder result = new StringBuilder();

        for (String component : components) {
            result.append(component.trim()).append(DASH);
        }

        return result.substring(0, result.length() - 1);

    }

    /**
     * Load/Reload a YAML configuration.
     *
     * @param plugin              The not null instance of the plugin.
     * @param settingsManager     The settings manager instance. Null if you want to load it.
     * @param fileName            The not null config file name.
     * @param settingsHolderClass The not null Class of the SettingsHolder.
     * @return The not null loaded SettingsManager instance.
     */
    @NotNull
    public static SettingsManager reloadAndGetSettings(@NotNull Plugin plugin,
                                                       @Nullable SettingsManager settingsManager,
                                                       @NotNull String fileName,
                                                       @NotNull Class<? extends SettingsHolder> settingsHolderClass) {

        if (settingsManager == null) {

            File dataFolder = plugin.getDataFolder();
            File yamlFile = new File(dataFolder, fileName);

            settingsManager = SettingsManagerBuilder
                    .withYamlFile(yamlFile)
                    .configurationData(settingsHolderClass)
                    .useDefaultMigrationService()
                    .create();

        } else {
            settingsManager.reload();
        }

        return settingsManager;

    }

    /**
     * @param fileName The not null file name.
     * @return True if it ends with .yml.
     */
    public static boolean isYamlFile(@NotNull String fileName) {
        return fileName.toLowerCase(Locale.ROOT).endsWith(DOT + YAML_FILE_EXTENSION);
    }

    /**
     * Check if the settings manager is not null.
     *
     * @param settingsManager The settingsManager to check.
     * @param fileName The configuration file name.
     * @throws IllegalStateException If the settings manager instance is null,
     *                               so if the configuration is not loaded.
     */
    public static void isLoadedSettingsManager(@Nullable SettingsManager settingsManager, @NotNull String fileName) {

        if (settingsManager != null) {
            return;
        }

        throw new IllegalStateException("the config " + fileName + " is not loaded");

    }

}
