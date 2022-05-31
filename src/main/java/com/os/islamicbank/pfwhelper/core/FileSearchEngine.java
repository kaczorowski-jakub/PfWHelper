package com.os.islamicbank.pfwhelper.core;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

interface FileSearchEngine {
    List<File> searchForFiles(String path);
    List<File> searchForFiles(final String path, final FileFilter fileFilter);
    List<File> filterFiles(List<File> files, String pattern, Boolean caseSensitive);
}
