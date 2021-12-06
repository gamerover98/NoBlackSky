package it.gamerover.nbs.reflection;

import it.gamerover.nbs.reflection.craftbukkit.CBCraftServer;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftVersion;
import it.gamerover.nbs.reflection.minecraft.MCSharedConstants;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionContainer {

    private static final String DOT = ".";
    private static final String COMMA = ",";

    /**
     * The version name from the package: org.bukkit.craftbukkit.1_#_R§
     *
     * Where:
     * - # is the version (8, 12, 17, ...)
     * - § is the Revision (1, 2, 3, ...)
     */
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

        @NotNull
        private final MCMinecraftServer minecraftServer;

        @NotNull
        private final MCSharedConstants sharedConstants;

        /**
         * The net.minecraft.server.MinecraftVersion class is
         * available from Spigot 1.17.
         */
        @Nullable
        private MCMinecraftVersion minecraftVersion;

        private Minecraft(String completeServerVersion) throws ReflectionException {

            Object minecraftServerInstance = craftBukkit.getCraftServer().getMinecraftServerInstance();

            this.minecraftServer = new MCMinecraftServer(completeServerVersion, minecraftServerInstance);
            this.sharedConstants = new MCSharedConstants(completeServerVersion);

            Object gameVersionInstance = sharedConstants.getGameVersionInstance();

            // MinecraftVersion class is available from Spigot 1.17.
            if (gameVersionInstance != null) {
                this.minecraftVersion = new MCMinecraftVersion(completeServerVersion, gameVersionInstance);
            }

        }

    }

    /**
     * Gets the version name from the package: org.bukkit.craftbukkit.1_#_R§
     *
     * Where:
     * - # is the version (8, 12, 17, ...)
     * - § is the Revision (1, 2, 3, ...)
     *
     * @return The internal minecraft version of the craftbukkit package name.
     */
    @NotNull
    private static String calculateServerVersion() {

        Server server = Bukkit.getServer();
        String serverClassName = server.getClass().getPackage().getName();

        return serverClassName.replace(DOT, COMMA).split(COMMA)[3];

    }

}
