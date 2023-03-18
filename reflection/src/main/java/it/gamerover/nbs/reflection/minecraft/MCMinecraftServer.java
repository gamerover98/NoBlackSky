package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.RawServerVersion;
import it.gamerover.nbs.reflection.ReflectionException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public final class MCMinecraftServer extends MCReflection {

    private static final String MC_SUB_PACKAGE              = "server";
    private static final String MINECRAFT_SERVER_CLASS_NAME = "MinecraftServer";
    private static final String GET_VERSION_METHOD_NAME     = "getVersion";

    /**
     * Gets the server version.
     *
     * This field is the same that can be found in MinecraftVersion.getName().
     * <b>The getVersion() method has been removed from spigot 1.18.</b>
     *
     * <bNull</b> if the server version is 1.18 or later.
     *
     */
    @Getter @Nullable
    private final String version;

    @SuppressWarnings("squid:S2637")
    public MCMinecraftServer(@NotNull RawServerVersion rawServerVersion,
                             @NotNull Object minecraftServerInstance) throws ReflectionException {

        super(rawServerVersion, MC_SUB_PACKAGE);

        Class<?> minecraftServerClass = super.getMinecraftClass(MINECRAFT_SERVER_CLASS_NAME);
        String getVersionMethodResult = null;

        try {

            Method getVersionMethod = super.getMethod(minecraftServerClass, GET_VERSION_METHOD_NAME);
            getVersionMethodResult = (String) getVersionMethod.invoke(minecraftServerInstance);

        } catch (Exception ex) {
            // Nothing to do, from Spigot 1.18 this method is unavailable.
            // If you want to know the server version, check the MinecraftVersion class.
        }

        this.version = getVersionMethodResult;

    }

}
