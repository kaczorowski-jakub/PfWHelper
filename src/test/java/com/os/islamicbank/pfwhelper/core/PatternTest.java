package com.os.islamicbank.pfwhelper.core;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatternTest {

    @Test
    public void testDataobjectPattern() {
        // given
        String dataobjectPattern = ".*(\\s+)dataobject.(\\s)*=(\\s)*\"zd_cifinq_defrecord_detail\".*";
        String line = "string dataobject = \"zd_cifinq_defrecord_detail\"";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testGlobaltypePattern() {
        // given
        String globaltypePattern = "dataobject(\\s)*=";
        String line = "";

        // when
        boolean found = line.matches(globaltypePattern);

        // then
        assertTrue(found);
    }

}
