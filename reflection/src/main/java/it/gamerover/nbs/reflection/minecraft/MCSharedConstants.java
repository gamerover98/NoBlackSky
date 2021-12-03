package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.ReflectionException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * The SharedConstants class from net.minecraft.* is available from Spigot 1.8, but
 * it is used from Spigot 1.17!
 */
public final class MCSharedConstants extends MCReflection {

    private static final String SHARED_CONSTANTS_CLASS_NAME  = "SharedConstants";
    private static final String GET_GAME_VERSION_METHOD_NAME_N1 = "getGameVersion";
    private static final String GET_GAME_VERSION_METHOD_NAME_N2 = "b";

    /**
     * Gets the GameVersion instance from the current running server.
     * This field is null prior to Spigot 1.17.
     */
    @Getter @Nullable
    private final Object gameVersionInstance;

    @SuppressWarnings("squid:S2637")
    public MCSharedConstants(@NotNull String completeServerVersion) throws ReflectionException {

        super(completeServerVersion);

        // If it is after 1.16.5
        if (!isBefore1_17(completeServerVersion)) {
            this.gameVersionInstance = obtainGameVersion();
        } else {
            this.gameVersionInstance = null;
        }

    }

    private Object obtainGameVersion() throws ReflectionException {

        Class<?> sharedConstantsClass = super.getMinecraftClass(SHARED_CONSTANTS_CLASS_NAME);
        Method getGameVersionMethod;

        try {

            getGameVersionMethod = super.getMethod(sharedConstantsClass, GET_GAME_VERSION_METHOD_NAME_N1);
            return getGameVersionMethod.invoke(null);

        } catch (Exception ex1) {

            StringBuilder errorMessage = new StringBuilder("Cannot find: ");

            errorMessage.append(getMinecraftPackage());
            errorMessage.append('.');
            errorMessage.append(SHARED_CONSTANTS_CLASS_NAME);
            errorMessage.append('.');
            errorMessage.append(GET_GAME_VERSION_METHOD_NAME_N1);
            errorMessage.append("() ");

            try {

                getGameVersionMethod = super.getMethod(sharedConstantsClass, GET_GAME_VERSION_METHOD_NAME_N2);
                return getGameVersionMethod.invoke(null);

            } catch (Exception ex2) {

                errorMessage.append("or ");
                errorMessage.append(GET_GAME_VERSION_METHOD_NAME_N2);
                errorMessage.append("() ");
                errorMessage.append("methods");

            }

            throw new ReflectionException(errorMessage.toString(), ex1);

        }

    }

}
