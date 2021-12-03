package it.gamerover.nbs.reflection;

import it.gamerover.nbs.reflection.craftbukkit.CBCraftServer;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public final class ReflectionContainer {

    private static final String DOT = ".";
    private static final String COMMA = ",";

    private static final String COMPLETE_SERVER_VERSION = calculateServerVersion();

    /**
     * Gets the CraftBukkit reflections.
     */
    @Getter @NotNull
    private final CraftBukkit craftBukkit;

    /**
     * Gets the Minecraft reflections.
     */
    @Getter @NotNull
    private final Minecraft minecraft;

    public ReflectionContainer() throws ReflectionException {

        this.craftBukkit = new CraftBukkit(COMPLETE_SERVER_VERSION);
        this.minecraft   = new Minecraft(COMPLETE_SERVER_VERSION);

    }

    @Getter
    @SuppressWarnings("InnerClassMayBeStatic")
    public final class CraftBukkit {

        private final CBCraftServer craftServer;

        private CraftBukkit(String completeServerVersion) throws ReflectionException {
            this.craftServer = new CBCraftServer(completeServerVersion);
        }

    }

    @Getter
    public final class Minecraft {

        private final MCMinecraftServer minecraftServer;

        private Minecraft(String completeServerVersion) throws ReflectionException {

            Object minecraftServerInstance = craftBukkit.getCraftServer().getMinecraftServerInstance();
            this.minecraftServer = new MCMinecraftServer(completeServerVersion, minecraftServerInstance);

        }

    }

    /**
     * @return The internal minecraft version of the craftbukkit package name.
     */
    private static String calculateServerVersion() {

        Server server = Bukkit.getServer();
        String serverClassName = server.getClass().getPackage().getName();

        return serverClassName.replace(DOT, COMMA).split(COMMA)[3];

    }

}
