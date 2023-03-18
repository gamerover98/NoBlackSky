package it.gamerover.nbs.reflection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * From CraftBukkit you can get the raw server version and revision.
 * E.g. 1.19.4 is v1_19_R3 as raw server version.
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class RawServerVersion {

    /**
     * The raw server version, e.g. 1.19_R3 (without starting 'v').
     */
    private final String raw;

    /**
     * Gets the version number of the server version.
     * E.g. for Spigot 1_19_R3, the version number is 19.
     */
    private final Integer versionNumber;

    /**
     * Gets the revision of the server version.
     * E.g. for Spigot 1_19_R3, the revision is R3.
     */
    private final String revision;

    /**
     * @param rawServerVersion The raw server version from the craftbukkit package (e.g. v1_19_R3)
     * @throws ReflectionException If server version has an unknown format.
     */
    public RawServerVersion(@NotNull String rawServerVersion) throws ReflectionException {
        this.raw = rawServerVersion.substring(1); // removing starting 'v'.
        String[] split = this.raw.split("_"); // split 1_19_R3

        try {
            this.versionNumber = Integer.parseInt(split[1]); // 19
        } catch (NumberFormatException numberFormatEx) {
            throw new ReflectionException("Unknown raw server version", numberFormatEx);
        }

        this.revision = split[2]; // R3
    }

    /**
     * Gets the revision number of the current revision string.
     * @return The number after the R -> e.g. 1_19_R3, the revision number is 3.
     * @throws ReflectionException If the revision has an unexpected value.
     */
    public Integer getRevisionNumber() throws ReflectionException {

        if (revision.startsWith("R")) {
            try {
                return Integer.parseInt(this.revision.substring(1));
            } catch (NumberFormatException  numberFormatEx) {
                throw new ReflectionException("Can't parse to int the server revision number version", numberFormatEx);
            }
        }

        throw new ReflectionException("Unsupported server revision " + this.revision);
    }
}