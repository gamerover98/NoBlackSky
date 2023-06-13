package it.gamerover.nbs.reflection;

import it.gamerover.nbs.reflection.minecraft.MCMinecraftServer;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftVersion;
import it.gamerover.nbs.reflection.util.ReflectionUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * You can find protocol and data version
 * at <a href="https://minecraft.gamepedia.com/Protocol_version">Minecraft protocol versions</a>
 */
@ToString
@SuppressWarnings({"squid:S00100", "squid:S1192", "unused"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServerVersion {

    // To indicate later versions.
    NEXT    ("next", null, Short.MAX_VALUE,Short.MAX_VALUE, false),

    // Add here the following versions ...
    V1_20_1 ("1.20.1",  getRawServerVersion("v1_20_R1"), 763, 3465, false),
    V1_20   ("1.20",    getRawServerVersion("v1_20_R1"), 763, 3463, false),

    V1_19_4 ("1.19.4",  getRawServerVersion("v1_19_R3"), 762, 3337, false),
    // Starting with Minecraft 1.19.3, Mojang decided to ruin developers' lives :/
    V1_19_3 ("1.19.3",  getRawServerVersion("v1_19_R2"), 761, 3218, false),
    V1_19_2 ("1.19.2",  getRawServerVersion("v1_19_R1"), 760, 3120, false),
    V1_19_1 ("1.19.1",  getRawServerVersion("v1_19_R1"), 760, 3117, false),
    V1_19   ("1.19",    getRawServerVersion("v1_19_R1"), 759, 3105, false),

    V1_18_2 ("1.18.2",  getRawServerVersion("v1_18_R2"), 758, 2975, false),
    V1_18_1 ("1.18.1",  getRawServerVersion("v1_18_R1"), 757, 2865, false),
    V1_18   ("1.18",    getRawServerVersion("v1_18_R1"), 757, 2860, false),

    V1_17_1 ("1.17.1",  getRawServerVersion("v1_17_R1"), 756, 2730, false),
    V1_17   ("1.17",    getRawServerVersion("v1_17_R1"), 755, 2724, false),

    V1_16_5 ("1.16.5",  getRawServerVersion("v1_16_R3"), 754, 2586, false),
    V1_16_4 ("1.16.4",  getRawServerVersion("v1_16_R3"), 754, 2584, false),
    V1_16_3 ("1.16.3",  getRawServerVersion("v1_16_R2"), 753, 2580, false),
    V1_16_2 ("1.16.2",  getRawServerVersion("v1_16_R2"), 751, 2578, false),
    V1_16_1 ("1.16.1",  getRawServerVersion("v1_16_R1"), 736, 2567, false),
    V1_16   ("1.16",    getRawServerVersion("v1_16_R1"), 735, 2566, false),

    V1_15_2 ("1.15.2",  getRawServerVersion("v1_15_R1"), 578, 2230, false),
    V1_15_1 ("1.15.1",  getRawServerVersion("v1_15_R1"), 575, 2227, false),
    V1_15   ("1.15",    getRawServerVersion("v1_15_R1"), 573, 2225, false),

    V1_14_4 ("1.14.4",  getRawServerVersion("v1_14_R1"), 498, 1976, false),
    V1_14_3 ("1.14.3",  getRawServerVersion("v1_14_R1"), 490, 1968, false),
    V1_14_2 ("1.14.2",  getRawServerVersion("v1_14_R1"), 485, 1963, false),
    V1_14_1 ("1.14.1",  getRawServerVersion("v1_14_R1"), 480, 1957, false),
    V1_14   ("1.14",    getRawServerVersion("v1_14_R1"), 477, 1952, false),

    V1_13_2 ("1.13.2",  getRawServerVersion("v1_13_R2"), 404, 1631, false),
    V1_13_1 ("1.13.1",  getRawServerVersion("v1_13_R2"), 401, 1628, false),
    V1_13   ("1.13",    getRawServerVersion("v1_13_R1"), 393, 1519, false),

    V1_12_2 ("1.12.2",  getRawServerVersion("v1_12_R1"), 340, 1343, true),
    V1_12_1 ("1.12.1",  getRawServerVersion("v1_12_R1"), 338, 1241, true),
    V1_12   ("1.12",    getRawServerVersion("v1_12_R1"), 335, 1139, true),

    V1_11_2 ("1.11.2",  getRawServerVersion("v1_11_R1"), 316, 922, true),
    V1_11_1 ("1.11.1",  getRawServerVersion("v1_11_R1"), 316, 921, true),
    V1_11   ("1.11",    getRawServerVersion("v1_11_R1"), 315, 819, true),

    V1_10_2 ("1.10.2",  getRawServerVersion("v1_10_R1"), 210, 512, true),
    V1_10_1 ("1.10.1",  getRawServerVersion("v1_10_R1"), 210, 511, true),
    V1_10   ("1.10",    getRawServerVersion("v1_10_R1"), 210, 510, true),

    V1_9_4  ("1.9.4",   getRawServerVersion("v1_9_R2"),  110, 184, true),
    V1_9_3  ("1.9.3",   getRawServerVersion("v1_9_R2"),  110, 183, true),
    V1_9_2  ("1.9.2",   getRawServerVersion("v1_9_R2"),  109, 176, true),
    V1_9_1  ("1.9.1",   getRawServerVersion("v1_9_R1"),  108, 175, true),
    V1_9    ("1.9",     getRawServerVersion("v1_9_R2"),  107, 169, true),

    // There is no DataVersion for minecraft 1.8.8
    V1_8_8  ("1.8.8",   getRawServerVersion("v1_8_R3"),  47, 0, true),
    // For versions that are too old...
    PREVIOUS("previous", null,  -1, -1, true);

    /**
     * Gets the textual version, like "1.12.2" or "1.16.5".
     */
    @Getter @NotNull
    private final String version;

    /**
     * Gets the raw server (from craftbukkit), like "1_19_R3".
     */
    @Getter @Nullable
    private final RawServerVersion rawServerVersion;

    /**
     * A protocol version number (PVN) is an integer used to check for
     * incompatibilities between the player's client and the server
     * they are trying to connect to.
     */
    @Getter
    private final int protocolVersion;

    /**
     * A data version, also known as a world version, is a positive integer
     * used in a world saved data to denote a specific version, and determines
     * whether the player should be warned about opening that world due
     * to client version incompatibilities.
     */
    @Getter
    private final int dataVersion;

    /**
     * True:  The server version is 1.12.2 or prior.
     * False: The server version is 1.13 or upper.
     */
    @Getter
    private final boolean legacy;

    //
    // STATIC METHODS
    //

    /**
     * Gets the current running ServerVersion instance.
     *
     * @param container The not-null Reflection Container instance.
     * @return The current running ServerVersion instance.
     *         Null if the server version is not supported.
     */
    @NotNull
    public static ServerVersion getRunningServerVersion(@NotNull ReflectionContainer container) throws ReflectionException {
        ServerVersion result = null;
        String stringVersion;

        ReflectionContainer.Minecraft minecraft = container.getMinecraft();
        MCMinecraftVersion minecraftVersion = minecraft.getMinecraftVersion();

        if (minecraftVersion != null) {
            stringVersion = minecraftVersion.getReleaseTarget();
        } else {

            MCMinecraftServer minecraftServer = minecraft.getMinecraftServer();
            stringVersion = minecraftServer.getVersion();

        }

        if (stringVersion != null) {
            result = ServerVersion.getVersion(stringVersion);
        }

        if (result == null) {
            RawServerVersion currentRawServerVersion = ReflectionUtil.findRawServerVersion();
            RawServerVersion firstLegacyVersion = getFirst(true).getRawServerVersion(); // 1.8.8
            assert firstLegacyVersion != null;

            int currentVersionNumber = currentRawServerVersion.getVersionNumber();
            int legacyVersionNumber = firstLegacyVersion.getVersionNumber();
            result = currentVersionNumber < legacyVersionNumber ? PREVIOUS : NEXT;
        }

        return result;
    }

    @Nullable
    public static ServerVersion getVersionWithDataVersion(int dataVersion) {
        ServerVersion[] versions = ServerVersion.values();

        for (int i = 0 ; i < versions.length - 1 ; i++) {

            ServerVersion currentVersion = versions[i];
            ServerVersion nextVersion = versions[i + 1];

            int currentDataVersion = currentVersion.getDataVersion();
            int nextDataVersion = nextVersion.getDataVersion();

            if (dataVersion >= currentDataVersion && dataVersion < nextDataVersion) {
                return currentVersion;
            }
        }

        ServerVersion latest = versions[versions.length - 1];
        int latestDataVersion = latest.getDataVersion();

        if (dataVersion >= latestDataVersion) {
            return latest;
        }

        return null;
    }

    /**
     * @return True if the server is a flat version.
     */
    public boolean isFlat() {
        return !legacy;
    }

    @Nullable
    public static ServerVersion getVersion(@NotNull String versionString) {

        for (ServerVersion version : ServerVersion.values()) {

            if (version.getVersion().equalsIgnoreCase(versionString)) {
                return version;
            }
        }

        return null;
    }

    @Nullable
    public static ServerVersion getVersion(int protocolVersion) {

        for (ServerVersion version : ServerVersion.values()) {

            if (version.getProtocolVersion() == protocolVersion) {
                return version;
            }
        }

        return null;
    }

    /**
     * @param v1 The first version. Cannot be null.
     * @param v2 The second version. Cannot be null.
     * @return True if each version is legacy or flat.
     */
    public static boolean isCompatible(@NotNull ServerVersion v1, @NotNull ServerVersion v2) {

        if (v1.isLegacy() && v2.isLegacy()) {
            return true;
        }

        return v1.isFlat() && v2.isFlat();
    }

    public static boolean isNext(@NotNull ServerVersion version) {
        return version.equals(NEXT);
    }

    public static boolean is1_20(@NotNull ServerVersion version) {
        switch (version) {
            case V1_20:
            case V1_20_1: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_19(@NotNull ServerVersion version) {

        switch (version) {
            case V1_19:
            case V1_19_1:
            case V1_19_2:
            case V1_19_3:
            case V1_19_4: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_18(@NotNull ServerVersion version) {

        switch (version) {
            case V1_18:
            case V1_18_1:
            case V1_18_2: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_17(@NotNull ServerVersion version) {

        switch (version) {
            case V1_17:
            case V1_17_1: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_16(@NotNull ServerVersion version) {

        switch (version) {
            case V1_16:
            case V1_16_1:
            case V1_16_2:
            case V1_16_3:
            case V1_16_4:
            case V1_16_5: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_15(@NotNull ServerVersion version) {

        switch (version) {
            case V1_15:
            case V1_15_1:
            case V1_15_2: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_14(@NotNull ServerVersion version) {

        switch (version) {
            case V1_14:
            case V1_14_1:
            case V1_14_2:
            case V1_14_3:
            case V1_14_4: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_13(@NotNull ServerVersion version) {

        switch (version) {
            case V1_13:
            case V1_13_1:
            case V1_13_2: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_12(@NotNull ServerVersion version) {

        switch (version) {
            case V1_12:
            case V1_12_1:
            case V1_12_2: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_11(@NotNull ServerVersion version) {

        switch (version) {
            case V1_11:
            case V1_11_1:
            case V1_11_2: return true;
            default: return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_10(@NotNull ServerVersion version) {

        switch (version) {
            case V1_10:
            case V1_10_1:
            case V1_10_2:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_9(@NotNull ServerVersion version) {

        switch (version) {
            case V1_9:
            case V1_9_1:
            case V1_9_2:
            case V1_9_3:
            case V1_9_4: return true;
            default: return false;
        }
    }

    public static boolean is1_8_8(@NotNull ServerVersion version) {
        return version == V1_8_8;
    }

    public static boolean isPrevious(@NotNull ServerVersion version) {
        return version.equals(PREVIOUS);
    }

    /**
     * Gets the latest of the legacy or flat Minecraft version.
     *
     * @param legacy True if you want the latest legacy Minecraft version.
     * @return The latest Minecraft version.
     */
    @NotNull
    public static ServerVersion getLatest(boolean legacy) {

        if (legacy) {
            return ServerVersion.V1_12_2;
        } else {
            ServerVersion[] versions = ServerVersion.values();
            return versions[1];
        }
    }

    /**
     * Gets the first supported (legacy or flat) Minecraft version.
     *
     * @param legacy True if you want the first legacy Minecraft version.
     * @return The first Minecraft version.
     */
    @NotNull
    public static ServerVersion getFirst(boolean legacy) {
        return legacy ? ServerVersion.V1_8_8 : ServerVersion.V1_13;
    }

    private static RawServerVersion getRawServerVersion(@NotNull String rawVersion) {

        try {
            return new RawServerVersion(rawVersion);
        } catch (ReflectionException reflectionEx) {
            // nothing to do.
        }

        return null;
    }
}
