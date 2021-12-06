package it.gamerover.nbs.reflection.util;

import it.gamerover.nbs.reflection.ServerVersion;
import org.jetbrains.annotations.NotNull;

public class ReflectionUtil {

    /**
     * Nothing to say, it is a dot.
     */
    private static final String DOT = "\\.";

    /**
     * Regex to check if the server version string is corrected.
     *
     * 1.16         YES
     * 1.16.0       YES
     * 1.16.0.0.0.0 YES
     *
     * 1.16.    NO
     * 1..16    NO
     * ..1.16   NO
     * 1.abc.16 NO
     * -1.16    NO
     *
     */
    @SuppressWarnings("squid:S5998") // this repetition can lead to a stack overflow for large inputs.
    private static final String SERVER_VERSION_REGEX = "^([0-9])([.][0-9]+)+";

    private ReflectionUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * Check if a server version is major than another.
     *
     * @param v1 The server version to compare to v2.
     * @param v2 The challenger server version.
     * @return MAJOR if v1 > v2, SAME if v1 = v2 and MINOR if v1 < v2.
     */
    @SuppressWarnings("squid:S135")
    public static Comparison compareServerVersions(@NotNull ServerVersion v1, @NotNull ServerVersion v2) {

        int dataVersionV1 = v1.getDataVersion();
        int dataVersionV2 = v2.getDataVersion();

        if (dataVersionV1 > dataVersionV2) {
            return Comparison.MAJOR;
        }

        if (dataVersionV1 < dataVersionV2) {
            return Comparison.MINOR;
        }

        return Comparison.SAME;

    }

    /**
     * Check if a server version is major than another.
     *
     * @param v1 The server version to compare to v2.
     * @param v2 The challenger server version.
     * @return MAJOR if v1 > v2, SAME if v1 = v2 and MINOR if v1 < v2.
     */
    @SuppressWarnings("squid:S135")
    public static Comparison compareServerVersions(@NotNull String v1, @NotNull String v2) {

        if (v1.isEmpty() || v2.isEmpty()) {
            throw new IllegalArgumentException("The minecraft version arguments cannot be empty");
        }

        if (!v1.matches(SERVER_VERSION_REGEX) || !v2.matches(SERVER_VERSION_REGEX)) {
            throw new IllegalArgumentException("Not supported minecraft version argument");
        }

        Comparison result = Comparison.SAME;
        int[] s1 = splitMinecraftServerVersion(v1);
        int[] s2 = splitMinecraftServerVersion(v2);
        boolean inverted = false;

        if (s2.length > s1.length) {

            int[] temp = s2;
            s2 = s1;
            s1 = temp;
            inverted = true;

        }

        for (int i = 0 ; i < s1.length ; i++) {

            int n1 = s1[i];
            int n2 = 0;

            if (i < s2.length) {
                n2 = s2[i];
            }

            if (n1 > n2) {

                result = Comparison.MAJOR;
                break;

            } else if (n1 < n2) {

                result = Comparison.MINOR;
                break;

            }

        }

        return inverted ? Comparison.getInverted(result) : result;

    }

    /**
     *
     * @param serverVersion The not null string of server version.
     * @return An array of each server version number. (ex: 1.16 -> [1, 16])
     */
    public static int[] splitMinecraftServerVersion(@NotNull String serverVersion) {

        if (serverVersion.isEmpty()) {
            throw new IllegalArgumentException("The server version argument cannot be empty");
        }

        String[] split = serverVersion.split(DOT);
        int[] result = new int[split.length];

        for (int i = 0 ; i < split.length ; i++) {
            result[i] = Integer.parseInt(split[i]);
        }

        return result;

    }

}
