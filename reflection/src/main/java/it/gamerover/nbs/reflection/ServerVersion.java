package it.gamerover.nbs.reflection;

import it.gamerover.nbs.reflection.minecraft.MCMinecraftServer;
import it.gamerover.nbs.reflection.minecraft.MCMinecraftVersion;
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
@SuppressWarnings({"squid:S00100", "unused"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServerVersion {

    // Add here the following versions ...
    V1_19  ("1.19",   759, 3105, false),

    V1_18_2("1.18.2", 758, 2975, false),
    V1_18_1("1.18.1", 757, 2865, false),
    V1_18  ("1.18",   757, 2860, false),

    V1_17_1("1.17.1", 756, 2730, false),
    V1_17  ("1.17",   755, 2724, false),

    V1_16_5("1.16.5", 754, 2586, false),
    V1_16_4("1.16.4", 754, 2584, false),
    V1_16_3("1.16.3", 753, 2580, false),
    V1_16_2("1.16.2", 751, 2578, false),
    V1_16_1("1.16.1", 736, 2567, false),
    V1_16  ("1.16",   735, 2566, false),

    V1_15_2("1.15.2", 578, 2230, false),
    V1_15_1("1.15.1", 575, 2227, false),
    V1_15  ("1.15",   573, 2225, false),

    V1_14_4("1.14.4", 498, 1976, false),
    V1_14_3("1.14.3", 490, 1968, false),
    V1_14_2("1.14.2", 485, 1963, false),
    V1_14_1("1.14.1", 480, 1957, false),
    V1_14  ("1.14",   477, 1952, false),

    V1_13_2("1.13.2", 404, 1631, false),
    V1_13_1("1.13.1", 401, 1628, false),
    V1_13  ("1.13",   393, 1519, false),

    V1_12_2("1.12.2", 340, 1343, true),
    V1_12_1("1.12.1", 338, 1241, true),
    V1_12  ("1.12",   335, 1139, true),

    V1_11_2("1.11.2", 316, 922, true),
    V1_11_1("1.11.1", 316, 921, true),
    V1_11  ("1.11",   315, 819, true),

    V1_10_2("1.10.2", 210, 512, true),
    V1_10_1("1.10.1", 210, 511, true),
    V1_10  ("1.10",   210, 510, true),

    V1_9_4 ("1.9.4",  110, 184, true),
    V1_9_3 ("1.9.3",  110, 183, true),
    V1_9_2 ("1.9.2",  109, 176, true),
    V1_9_1 ("1.9.1",  108, 175, true),
    V1_9   ("1.9",    107, 169, true),

    // There is no DataVersion for minecraft 1.8.8
    V1_8_8 ("1.8.8",  47, 0, true);

    /**
     * Gets the textual version, like "1.12.2" or "1.16.5".
     */
    @Getter @NotNull
    private final String version;

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
    @Nullable
    public static ServerVersion getRunningServerVersion(@NotNull ReflectionContainer container) {

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

    @SuppressWarnings("DuplicatedCode") // prevents checking code duplicates
    public static boolean is1_19(@NotNull ServerVersion version) {
        return version.equals(V1_19);
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
            case V1_10_2: return true;
            default: return false;
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
            return versions[0];

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

        if (legacy) {
            return ServerVersion.V1_8_8;
        } else {
            return ServerVersion.V1_13;
        }

    }

}
