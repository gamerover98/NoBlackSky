package it.gamerover.nbs.core.reflection.minecraft;

import it.gamerover.nbs.core.reflection.ReflectionException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public final class MCMinecraftServer extends MCReflection {

    private static final String MINECRAFT_SERVER_CLASS_NAME = "MinecraftServer";
    private static final String GET_VERSION_METHOD_NAME     = "getVersion";

    /**
     * Gets the server version.
     */
    @Getter @NotNull
    private final String version;

    @SuppressWarnings("squid:S2637")
    public MCMinecraftServer(@NotNull String completeServerVersion,
                             @NotNull Object minecraftServerInstance) throws ReflectionException {

        super(completeServerVersion);

        Class<?> minecraftServerClass = super.getMinecraftClass(MINECRAFT_SERVER_CLASS_NAME);

        try {

            Method getVersionMethod = super.getMethod(minecraftServerClass, GET_VERSION_METHOD_NAME);
            this.version = (String) getVersionMethod.invoke(minecraftServerInstance);

        } catch (Exception ex) {
            throw new ReflectionException(MINECRAFT_SERVER_CLASS_NAME + " - get version reflection error", ex);
        }

    }

}
