package it.gamerover.nbs;

import com.dumptruckman.minecraft.util.Logging;
import it.gamerover.nbs.reflection.ReflectionException;
import it.gamerover.nbs.reflection.ServerVersion;
import it.gamerover.nbs.support.flat.FlatHandler;
import it.gamerover.nbs.support.legacy.LegacyHandler;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 *
 * @author gamerover98
 *
 * The main class of the project.
 */
public class NoBlackSky extends JavaPlugin {

    private static final String PROTOCOL_LIB_PLUGIN_NAME = "ProtocolLib";

    /**
     * Gets the NoBlackSky plugin instance.
     */
    @Getter
    private static NoBlackSky instance;

    /**
     * Gets the plugin handler.
     *
     * > If the server is a legacy version (1.8.8 - 1.12.2) the handler will be LegacyHandler.
     * > If the server is a flat version (1.13+) the handler will be FlatHandler.
     */
    @Getter
    private CoreHandler handler;

    /**
     * If true, the plugin will execute the onEnable method.
     */
    private boolean isPluginStartable = true;

    @Override
    @SuppressWarnings("squid:S2696") // Make the enclosing method "static" or remove this set.
    public void onLoad() {

        Logging.init(this);
        NoBlackSky.instance = this;

        try {

            CoreHandler.init();

        } catch (ReflectionException firstEx) {

            String message = "Please report this error on the issues page on GitHub or Spigot forum";
            Logging.log(Level.SEVERE, message, firstEx);

            isPluginStartable = false;

        }

        PluginManager pluginManager = getServer().getPluginManager();
        boolean protocolLibInstalled = pluginManager.getPlugin(PROTOCOL_LIB_PLUGIN_NAME) != null;

        if (!protocolLibInstalled) {

            isPluginStartable = false;
            Logging.severe("Cannot start " + getName() + " because "
                    + PROTOCOL_LIB_PLUGIN_NAME + " isn't installed!");

        }

        if (!isPluginStartable) {
            return;
        }

        ServerVersion serverVersion = CoreHandler.getServerVersion();

        try {

            if (serverVersion.isLegacy()) {
                handler = new LegacyHandler(this);
            } else {
                handler = new FlatHandler(this);
            }

            handler.pluginLoading();

        } catch (Exception generalEx) {

            isPluginStartable = false;
            Logging.log(Level.SEVERE, "Cannot initialize the handler", generalEx);

        }


    }

    @Override
    public void onEnable() {

        if (!isPluginStartable) {

            disablePlugin();
            return;

        }

        handler.pluginEnabling();

    }

    @Override
    public void onDisable() {

        if (handler == null) {
            return;
        }

        handler.pluginDisabling();

    }

    /**
     * Disable the plugin with a warning message.
     */
    private void disablePlugin() {

        Logging.warning("Disabling " + getName() + " - " + super.getDescription().getVersion());
        super.setEnabled(false);

    }

}
