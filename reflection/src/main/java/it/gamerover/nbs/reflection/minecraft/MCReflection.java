package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.Reflection;
import it.gamerover.nbs.reflection.ReflectionException;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Specific reflection class for the Native Minecraft package.
 */
public class MCReflection extends Reflection {

    /**
     * The number of the 1.17 Caves & Cliffs update.
     */
    private static final int CAVES_AND_CLIFFS_UPDATE_NUMBER = 17;

    /**
     * The not compliant Native Minecraft package.
     * This string will be concatenated with the complete server version.
     */
    private static final String MINECRAFT_PACKAGE = "net.minecraft";

    private static final String SERVER_WORD = "server";

    /**
     * The not compliant Native Minecraft package.
     * This string will be concatenated with the complete server version.
     */
    private static final String BEFORE_1_17_MINECRAFT_PACKAGE = MINECRAFT_PACKAGE + '.' + SERVER_WORD;

    /**
     * The complete Native Minecraft package (Ex: net.minecraft.server.v1_8_R3.ClassName).
     *
     * <p>
     *     From 1.17 the Minecraft package doesn't contain
     *     the revision version (Ex: net.minecraft.server.ClassName)
     * </p>
     */
    @Getter(AccessLevel.PROTECTED)
    private final String minecraftPackage;


    /**
     * @param completeServerVersion The NMS complete server version like V1_8_R3
     */
    public MCReflection(@NotNull String completeServerVersion) {
        this(completeServerVersion, null);
    }

    /**
     * The completeServerVersion will be used until 1.16.5 to complete the NMS package.
     * > net.minecraft.v1_16_R1.*;
     *
     * From 1.17, the NMS package has changed: net.minecraft.server.*
     * > This new package has sub-packages like net.minecraft.server.something.everything;
     *
     * @param completeServerVersion The not-null NMS complete server version like V1_8_R3.
     * @param subPackage The nullable/empty string of a sub package of net.minecraft.*.#.
     */
    public MCReflection(@NotNull String completeServerVersion, @Nullable String subPackage) {

        if (subPackage == null) {
            subPackage = "";
        }

        subPackage = subPackage.trim();
        String pack;

        // Before 1.17
        if (isBefore1_17(completeServerVersion)) {

            // net.minecraft.server.v1_16_R3
            pack = BEFORE_1_17_MINECRAFT_PACKAGE + '.' + completeServerVersion;

            /*
             * From 1.8 to 1.16.5, if the sub-folder starts with "server", it will
             * be trimmed from string.
             *
             * Also, there are no sub-packages on the net.minecraft.server
             * package but, this feature is available.
             */
            if (subPackage.toLowerCase(Locale.ROOT).startsWith(SERVER_WORD)) {

                subPackage = subPackage.substring(SERVER_WORD.length());
                subPackage = subPackage.trim();

            }

        } else {
            pack = MINECRAFT_PACKAGE;
        }

        if (!subPackage.isEmpty()) {
            pack = pack + '.' + subPackage;
        }

        this.minecraftPackage = pack;

    }

    /**
     * Gets a Native Minecraft class from net.minecraft.server package.
     *
     * @param className The not null class name.
     * @return The not null Native class.
     * @throws ReflectionException If the class doesn't exist.
     */
    @NotNull
    public Class<?> getMinecraftClass(@NotNull String className) throws ReflectionException {
        return getMinecraftClass("", className);
    }

    /**
     * Gets a Native Minecraft class from net.minecraft.server package.
     *
     * @param packageName If in the future Spigot decide to add another sub-package, use it.
     *                    By default, this value is "".
     * @param className The not null class name.
     * @return The not null Native class.
     * @throws ReflectionException If the class doesn't exist.
     */
    @NotNull
    public Class<?> getMinecraftClass(@NotNull String packageName,
                                      @NotNull String className) throws ReflectionException {
        return super.getClass(this.minecraftPackage + packageName, className);
    }

    /**
     * @param version The not null complete server version: v1_16_R3
     * @return True if the server version is before 1.17 caves & cliffs update.
     */
    @SuppressWarnings("squid:S100") // SonarLint: Rename this method name to match the regular expression.
    protected static boolean isBefore1_17(@NotNull String version) {

        if (!version.startsWith("v")) {
            throw new IllegalArgumentException("The minecraft version needs to start with 'v'");
        }

        version = version.substring(1);
        String[] split = version.split("_");

        int firstNumber  = Integer.parseInt(split[0]);
        int secondNumber = Integer.parseInt(split[1]);

        if (firstNumber != 1) {
            throw new IllegalArgumentException("The minecraft versions needs to start with 1.*");
        }

        return secondNumber < CAVES_AND_CLIFFS_UPDATE_NUMBER;

    }

}