package com.os.islamicbank.pfwhelper.core;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
class FileSearchEngineImpl implements FileSearchEngine {

    @Override
    public List<File> searchForFiles(String path) {
        return searchForFiles(path, null);
    }

    @Override
    public List<File> searchForFiles(final String path, final FileFilter fileFilter) {
        final List<File> retList = new ArrayList<>();
        final File root = new File(path);

        if (root.exists() && root.isDirectory()) {
            final File[] fileArr = fileFilter != null ? root.listFiles(fileFilter) : root.listFiles();
            Arrays.asList(fileArr).forEach(file -> {
                if (file.isDirectory()) {
                    retList.addAll(searchForFiles(file.getPath()));
                } else {
                    retList.add(file);
                }
            });
        }

        return retList;
    }

    @Override
    public List<File> filterFiles(List<File> files, String pattern, Boolean caseSensitive) {
        final List<File> retList = new ArrayList<>();

        if (files == null) {
            return retList;
        }

        return files.stream().filter(file -> {
            if (caseSensitive && file.getName().matches(pattern)) {
                return true;
            } else if (!caseSensitive && file.getName().toUpperCase().matches(pattern.toUpperCase())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

    }
}
