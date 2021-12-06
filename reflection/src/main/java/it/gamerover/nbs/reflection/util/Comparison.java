package it.gamerover.nbs.reflection.util;

import org.jetbrains.annotations.NotNull;

public enum Comparison {

    MINOR, SAME, MAJOR;

    /**
     * @param comparison A not null instance of the enumeration.
     * @return If MINOR -> MAJOR, if MAJOR -> MINOR, SAME -> SAME.
     */
    public static Comparison getInverted(@NotNull Comparison comparison) {

        switch (comparison) {
            case MINOR: return MAJOR;
            case MAJOR: return MINOR;
            default:    return SAME;
        }

    }

}
