package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.ReflectionException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * The net.minecraft.MinecraftVersion class
 * implements com.mojang.bridge.game.GameVersion interface.
 */
public final class MCMinecraftVersion extends MCReflection {

    private static final String MINECRAFT_VERSION_CLASS_NAME   = "MinecraftVersion";
    private static final String GET_NAME_METHOD_NAME           = "getName";
    private static final String GET_RELEASE_TARGET_METHOD_NAME = "getReleaseTarget";

    /**
     * Gets the server version name.
     * For instance: 1.18-rc3
     */
    @Getter @NotNull
    private final String name;

    /**
     * Gets the server release target version.
     * For instance: 1.17, 1.17.1, 1.18, etc.
     */
    @Getter @NotNull
    private final String releaseTarget;

    @SuppressWarnings("squid:S2637")
    public MCMinecraftVersion(@NotNull String completeServerVersion,
                              @NotNull Object gameVersionInstance) throws ReflectionException {

        super(completeServerVersion);

        Class<?> minecraftVersionClass = super.getMinecraftClass(MINECRAFT_VERSION_CLASS_NAME);

        try {

            Method getNameMethod = super.getMethod(minecraftVersionClass, GET_NAME_METHOD_NAME);
            this.name = (String) getNameMethod.invoke(gameVersionInstance);

        } catch (Exception ex) {

            String errorMessage = getMinecraftPackage() + "." + MINECRAFT_VERSION_CLASS_NAME
                    + "." + GET_NAME_METHOD_NAME + "() method";
            throw new ReflectionException(errorMessage, ex);

        }

        try {

            Method getReleaseTargetMethod = super.getMethod(minecraftVersionClass, GET_RELEASE_TARGET_METHOD_NAME);
            this.releaseTarget = (String) getReleaseTargetMethod.invoke(gameVersionInstance);

        } catch (Exception ex) {

            String errorMessage = getMinecraftPackage() + "." + MINECRAFT_VERSION_CLASS_NAME
                    + "." + GET_RELEASE_TARGET_METHOD_NAME + "() method";
            throw new ReflectionException(errorMessage, ex);

        }

    }

}
