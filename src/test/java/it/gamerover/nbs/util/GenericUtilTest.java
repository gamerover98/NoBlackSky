package it.gamerover.nbs.util;

import org.junit.jupiter.api.Test;

import it.gamerover.nbs.util.GenericUtil.Comparison;

import static org.junit.jupiter.api.Assertions.*;

class GenericUtilTest {

    @Test
    void testCompareServerVersion() {

        assertThrows(IllegalArgumentException.class, () -> GenericUtil.compareServerVersions("", ""));
        assertThrows(IllegalArgumentException.class, () -> GenericUtil.compareServerVersions("1", "1"));
        assertThrows(IllegalArgumentException.class, () -> GenericUtil.compareServerVersions("1.16a", "1.15"));
        assertThrows(IllegalArgumentException.class, () -> GenericUtil.compareServerVersions("1me3fsv", "1,as"));
        assertThrows(IllegalArgumentException.class, () -> GenericUtil.compareServerVersions("-1.16", "1.16"));

        assertEquals(Comparison.SAME,  GenericUtil.compareServerVersions("1.16", "1.16"));
        assertEquals(Comparison.MAJOR, GenericUtil.compareServerVersions("1.17", "1.16"));
        assertEquals(Comparison.MINOR, GenericUtil.compareServerVersions("1.16", "1.17"));

        assertEquals(Comparison.SAME, GenericUtil.compareServerVersions("1.16.0", "1.16"));

        assertEquals(Comparison.SAME,  GenericUtil.compareServerVersions("1.16.0.0.0.0.0.0", "1.16.0.0.0"));
        assertEquals(Comparison.MAJOR, GenericUtil.compareServerVersions("1.16.0.0.0.0.0.1", "1.16.0.0.0"));
        assertEquals(Comparison.MINOR, GenericUtil.compareServerVersions("1.15.0.0.0.0.0.1", "1.16.0.0.0"));

        assertEquals(Comparison.MAJOR, GenericUtil.compareServerVersions("2.0", "1.16"));

    }

}
