package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.RawServerVersion;
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
    private static final String GET_NAME_METHOD_NAME_1         = "getName";
    private static final String GET_NAME_METHOD_NAME_2         = "c"; // 1.19.4 'getName' method name.
    private static final String GET_RELEASE_TARGET_METHOD_NAME = "getReleaseTarget";

    /**
     * Gets the server version name.
     * For instance: 1.18-rc3
     */
    @Getter @NotNull
    private final String name;

    /**
     * Gets the server release target version.
     * For instance: 1.17, 1.17.1, 1.18, 1.19 etc.
     * <p>
     *     From Spigot 1.19.3, this field doesn't exist and will be valued with the name.
     * </p>
     */
    @Getter @NotNull
    private final String releaseTarget;

    @SuppressWarnings("squid:S2637")
    public MCMinecraftVersion(@NotNull RawServerVersion rawServerVersion,
                              @NotNull Object gameVersionInstance) throws ReflectionException {

        super(rawServerVersion);

        Class<?> minecraftVersionClass = super.getMinecraftClass(MINECRAFT_VERSION_CLASS_NAME);
        this.name = getName(minecraftVersionClass, rawServerVersion, gameVersionInstance);
        String releaseTargetLocal = name;

        try {

            Method getReleaseTargetMethod = super.getMethod(minecraftVersionClass, GET_RELEASE_TARGET_METHOD_NAME);
            releaseTargetLocal = (String) getReleaseTargetMethod.invoke(gameVersionInstance);

        } catch (Exception ex) { // nothing to do.
            /* String errorMessage = getMinecraftPackage() + "." + MINECRAFT_VERSION_CLASS_NAME
             *        + "." + GET_RELEASE_TARGET_METHOD_NAME + "() method";
             * throw new ReflectionException(errorMessage, ex);
            */
        } finally {
            this.releaseTarget = releaseTargetLocal;
        }
    }

    private String getName(Class<?> minecraftVersionClass,
                           RawServerVersion rawServerVersion,
                           Object gameVersionInstance) throws ReflectionException {
        String getNameMethodName;

        // if version is lower or equal than 1.19.3
        if (rawServerVersion.getVersionNumber() < 19
                || (rawServerVersion.getVersionNumber() == 19 && rawServerVersion.getRevisionNumber() < 3)) {
            getNameMethodName = GET_NAME_METHOD_NAME_1;
        } else { // 1.19.4+
            getNameMethodName = GET_NAME_METHOD_NAME_2;
        }

        try {

            Method getNameMethod = super.getMethod(minecraftVersionClass, getNameMethodName);
            return (String) getNameMethod.invoke(gameVersionInstance);

        } catch (Exception ex) {
            String errorMessage = getMinecraftPackage()
                    + "." + MINECRAFT_VERSION_CLASS_NAME
                    + "." + getNameMethodName + "() method";
            throw new ReflectionException(errorMessage, ex);
        }
    }
}
