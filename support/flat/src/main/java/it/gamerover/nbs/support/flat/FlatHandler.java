package it.gamerover.nbs.support.flat;

import it.gamerover.nbs.CoreHandler;
import it.gamerover.nbs.core.packet.NoBlackSkyAdapter;
import it.gamerover.nbs.reflection.ServerVersion;
import it.gamerover.nbs.support.flat.packet.FlatNoBlackSkyAdapter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused") // This class is used from the deployment (deploy) module into the main class.
public class FlatHandler extends CoreHandler {

    private final FlatNoBlackSkyAdapter flatNoBlackSkyAdapter;

    public FlatHandler(@NotNull Plugin plugin) {

        super(plugin);

        ServerVersion currentVersion = getServerVersion();
        flatNoBlackSkyAdapter = new FlatNoBlackSkyAdapter(plugin, currentVersion);

    }

    @NotNull
    @Override
    protected NoBlackSkyAdapter getNoBlackSkyAdapter() {
        return this.flatNoBlackSkyAdapter;
    }

}