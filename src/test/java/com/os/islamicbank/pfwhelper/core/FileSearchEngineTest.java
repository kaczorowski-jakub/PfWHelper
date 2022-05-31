package com.os.islamicbank.pfwhelper.core;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class FileSearchEngineTest {


    private FileSearchEngine searchEngine = new FileSearchEngineImpl();
    private static final String PATH = "src/test/resources/test";

    @Test
    @DisplayName("Testing if search returns all the files from a given dir and sub-dirs")
    public void testSearchForAllTheFilesInGivenDirAndSubDirs() {
        // given + when
        List<File> list = searchEngine.searchForFiles(PATH);

        // then
        assertThat(list, hasSize(9));
    }

    @Test
    @DisplayName("Testing if the filters work for case insensitive scenario")
    public void testFilterFilesByPatternAndCaseInsensitive() {
        // given
        final List<File> inlist = searchEngine.searchForFiles(PATH);
        final String pattern = "z.*\\.srd";
        final Boolean caseSensitive = false;

        // when
        List<File> outList = searchEngine.filterFiles(inlist, pattern, caseSensitive);

        // then
        assertThat(outList, hasSize(5));
    }

}
