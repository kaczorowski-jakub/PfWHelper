package com.os.islamicbank.pfwhelper.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@Slf4j
class AnalyzeProcessorImpl implements AnalyzeProcessor {

    private FileSearchEngine fileSearchEngine;

    @Value("${dataobject.pattern}")
    private String dataobjectPattern;

    @Value("${globaltype.pattern}")
    private String globaltypePattern;

    @Autowired
    public AnalyzeProcessorImpl(final FileSearchEngine fileSearchEngine) {
        this.fileSearchEngine = fileSearchEngine;
    }

    @Override
    public Analysis analyze(List<File> dataWindowFiles, List<File> userObjectFiles, List<File> customDataWindowFiles) {

        for (File customDataWindowFile : customDataWindowFiles) {
            // here is a place to split into threads


            List<File> userObjectFile = findDataWindowInUserObjects(userObjectFiles, customDataWindowFile);
        }
        return null;
    }

    private List<File> findDataWindowInUserObjects(List<File> userObjectFiles, File customDataWindowFile) {
        for (File userObjectFile : userObjectFiles) {
            List<String> lines = findDataWindowInUserObject(userObjectFile, customDataWindowFile);
            if (lines != null && lines.size() > 0) {

            }
        }

        return null;
    }

    private List<String> findDataWindowInUserObject(File userObjectFile, File customDataWindowFile) {
        List<String> lines = new ArrayList<>();
        String searchedNamePattern = dataobjectPattern + "\"" + fileToObjectName(customDataWindowFile) + "\".*";

        try (Scanner reader = new Scanner(userObjectFile)){
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains(searchedNamePattern)) {
                    log.info(userObjectFile.getName() + " - " + line);
                }
                if (line.matches(globaltypePattern)) {
                    log.info(userObjectFile.getName() + " - " + line);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: " + userObjectFile.getAbsolutePath());
        }

        // TODO: how to search for the
        return lines;
    }

    private String fileToObjectName(File file) {
        return file.getName().substring(0, file.getName().indexOf("."));
    }
}
