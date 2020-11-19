package it.gamerover.nbs.util;

import org.jetbrains.annotations.NotNull;

public final class GenericUtil {

    private static final String DOT = "\\.";

    private GenericUtil() {
        throw new IllegalStateException("This is a static class");
    }

    public static Comparison compareServerVersions(@NotNull String v1, @NotNull String v2) {

        if (v1.isEmpty() || v2.isEmpty()) {
            throw new IllegalArgumentException("The minecraft version arguments cannot be empty");
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
            int n2 = s2[i];

            if (n1 > n2) {
                result = Comparison.MAJOR;
            } else if (n1 < n2) {
                result = Comparison.MINOR;
            }

        }

        return inverted ? Comparison.getInverted(result) : result;

    }

    private static int[] splitMinecraftServerVersion(@NotNull String serverVersion) {

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

    public enum Comparison {

        MINOR, SAME, MAJOR;

        public static Comparison getInverted(@NotNull Comparison comparison) {

            switch (comparison) {
                case MINOR: return MAJOR;
                case MAJOR: return MINOR;
                default: return SAME;
            }

        }

    }

}
