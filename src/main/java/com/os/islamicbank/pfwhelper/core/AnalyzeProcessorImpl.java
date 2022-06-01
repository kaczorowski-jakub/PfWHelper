package com.os.islamicbank.pfwhelper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
class AnalyzeProcessorImpl implements AnalyzeProcessor {

    private FileSearchEngine fileSearchEngine;

    @Autowired
    public AnalyzeProcessorImpl(final FileSearchEngine fileSearchEngine) {
        this.fileSearchEngine = fileSearchEngine;
    }

    @Override
    public Analysis analyze(List<File> dataWindowFiles, List<File> userObjectFiles, List<File> customDataWindowFiles) {

        for (File customDataWindowFile : customDataWindowFiles) {
            // place to split into threads

            List<File> userObjectFile = findDataWindowInUserObjects(userObjectFiles, fileToObjectName(customDataWindowFile));


        }
        return null;
    }

    private List<File> findDataWindowInUserObjects(List<File> userObjectFiles, String customDataWindowObjectName) {
        for (File userObjectFile : userObjectFiles) {
            List<String> lines = findDataWindowInUserObject(userObjectFile, customDataWindowObjectName);
            if (lines != null && lines.size() > 0) {

            }
        }

        return null;
    }

    private List<String> findDataWindowInUserObject(File userObjectFile, String customDataWindowObjectName) {
        List<String> lines = new ArrayList<>();
        // TODO: how to search for the
        return lines;
    }

    private String fileToObjectName(File file) {
        return file.getName().substring(0, file.getName().indexOf("."));
    }
}
