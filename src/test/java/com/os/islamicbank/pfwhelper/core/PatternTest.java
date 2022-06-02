package com.os.islamicbank.pfwhelper.core;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatternTest {

    @Test
    public void testDataobjectPattern() {
        // given
        String objectName = "zd_cifinq_defrecord_detail";
        String dataobjectPattern = ".*(\\s+)dataobject(\\s)*=(\\s)*\"" + objectName + "\".*";
        String line = "string dataobject = \"zd_cifinq_defrecord_detail\"";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testDataobjectPatternCaseSensitive() {
        // given
        String objectName = "zd_cifinq_defrecord_detail";
        String dataobjectPattern = ".*(\\s+)[d|D][a|A][t|T][a|A][o|O][b|B][j|J][e|E][c|C][t|T](\\s)*=(\\s)*\"" + objectName + "\".*";
        String line = "string DaTAObjeCT = \"zd_cifinq_defrecord_detail\"";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testGlobaltypePattern() {
        // given
        String objectName = "zuo_cifinq_defrecord_detail";
        String globaltypePattern = "(\\s)*global(\\s)+type(\\s)+" + objectName + "(\\s)+from(\\s)+.*";
        String line = "global type zuo_cifinq_defrecord_detail from uo_inquirymaintain";

        // when
        boolean found = line.matches(globaltypePattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testGlobaltypePatternCaseSensitive() {
        // given
        String objectName = "zuo_cifinq_defrecord_detail";
        String globaltypePattern = "(\\s)*[g|G][l|L][o|O][b|B][a|A][l|L](\\s)+[t|T][y|Y][p|P][e|E](\\s)+" + objectName + "(\\s)+[f|F][r|R][o|O][m|M](\\s)+.*";
        String line = "GlObAl   TyPE zuo_cifinq_defrecord_detail FRom  uo_inquirymaintain";

        // when
        boolean found = line.matches(globaltypePattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testDataobjectPatternPrefix() {
        // given
        String dataobjectPattern = ".*(\\s+)dataobject(\\s)*=(\\s)*\".*";
        String line = "string dataobject = \"zd_cifinq_defrecord_detail\"";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testGlobaltypePatternNoObject() {
        // given
        String dataobjectPattern = "(\\s)*global(\\s)+type(\\s)+.*(\\s)+from(\\s)+.*";
        String line = "global type zuo_cifinq_defrecord_detail from uo_inquirymaintain";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }

    @Test
    public void testDataobjectPatternNoObject() {
        // given
        String dataobjectPattern = ".*(\\s+)dataobject(\\s)*=(\\s)*\".*\".*";
        String line = "string dataobject = \"zd_cifinq_defrecord_detail\"";

        // when
        boolean found = line.matches(dataobjectPattern);

        // then
        assertTrue(found);
    }


}
