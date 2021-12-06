package it.gamerover.nbs.support.legacy;

import it.gamerover.nbs.CoreHandler;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import it.gamerover.nbs.support.legacy.packet.LegacyNoBlackSkyAdapter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused") // This class is used from the deployment (deploy) module into the main class.
public class LegacyHandler extends CoreHandler {

    private final LegacyNoBlackSkyAdapter legacyNoBlackSkyAdapter;

    public LegacyHandler(@NotNull Plugin plugin) {

        super(plugin);

        ServerVersion currentVersion = getServerVersion();
        legacyNoBlackSkyAdapter = new LegacyNoBlackSkyAdapter(plugin, currentVersion);

    }

    @NotNull
    @Override
    protected NoBlackSkyAdapter getNoBlackSkyAdapter() {
        return this.legacyNoBlackSkyAdapter;
    }

}