package com.os.islamicbank.pfwhelper.core;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class AnalyzeFacade {

    @Value("${file.pattern.srd}")
    private String srdFilePattern;

    @Value("${file.pattern.sru}")
    private String sruFilePattern;

    @Value("${file.pattern.srd.custom}")
    private String srdCustomFilePattern;

    private final AnalyzeProcessor analyzeProcessor;
    private final FileSearchEngine fileSearchEngine;

    @Autowired
    public AnalyzeFacade(final AnalyzeProcessor analyzeProcessor, final FileSearchEngine fileSearchEngine) {
        this.analyzeProcessor = analyzeProcessor;
        this.fileSearchEngine = fileSearchEngine;
    }

    public Report analyze(String path) {
        List<File> allFiles = fileSearchEngine.searchForFiles(path);
        List<File> srdFiles = fileSearchEngine.filterFiles(allFiles, srdFilePattern, false);
        List<File> sruFiles = fileSearchEngine.filterFiles(allFiles, sruFilePattern, false);
        List<File> srdCustomFiles = fileSearchEngine.filterFiles(srdFiles, srdCustomFilePattern, false);

        return analyzeProcessor.analyze(srdFiles, sruFiles, srdCustomFiles);
    }
}
