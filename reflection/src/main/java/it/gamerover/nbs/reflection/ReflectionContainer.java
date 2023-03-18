package it.gamerover.nbs.reflection;

import it.gamerover.nbs.reflection.craftbukkit.CBCraftServer;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftVersion;
import it.gamerover.nbs.reflection.minecraft.MCSharedConstants;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftServer;
import it.gamerover.nbs.reflection.util.ReflectionUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionContainer {

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

        RawServerVersion rawServerVersion = ReflectionUtil.findRawServerVersion();

        this.craftBukkit = new CraftBukkit(rawServerVersion);
        this.minecraft   = new Minecraft(rawServerVersion);
    }

    @Getter
    @SuppressWarnings("InnerClassMayBeStatic")
    public final class CraftBukkit {

        private final CBCraftServer craftServer;

        private CraftBukkit(RawServerVersion rawServerVersion) throws ReflectionException {
            this.craftServer = new CBCraftServer(rawServerVersion);
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

        private Minecraft(RawServerVersion rawServerVersion) throws ReflectionException {

            Object minecraftServerInstance = craftBukkit.getCraftServer().getMinecraftServerInstance();

            this.minecraftServer = new MCMinecraftServer(rawServerVersion, minecraftServerInstance);
            this.sharedConstants = new MCSharedConstants(rawServerVersion);

            Object gameVersionInstance = sharedConstants.getGameVersionInstance();

            // MinecraftVersion class is available from Spigot 1.17.
            if (gameVersionInstance != null) {
                this.minecraftVersion = new MCMinecraftVersion(rawServerVersion, gameVersionInstance);
            }
        }
    }
}
