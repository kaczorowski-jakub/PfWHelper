package com.os.islamicbank.pfwhelper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
class AnalyzeProcessorImpl implements AnalyzeProcessor {

    private FileSearchEngine fileSearchEngine;

    @Autowired
    public AnalyzeProcessorImpl(final FileSearchEngine fileSearchEngine) {
        this.fileSearchEngine = fileSearchEngine;
    }

    @Override
    public Analysis analyze(List<File> srdList, List<File> sruList) {
        return null;
    }
}
