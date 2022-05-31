package com.os.islamicbank.pfwhelper.core;

import com.os.islamicbank.pfwhelper.core.dto.AnalysisDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class AnalyzeFacade {

    @Value("file.pattern.srd")
    private String srdFilePattern;

    @Value("file.pattern.sru")
    private String sruFilePattern;

    private AnalyzeProcessor analyzeProcessor;
    private FileSearchEngine fileSearchEngine;

    @Autowired
    public AnalyzeFacade(final AnalyzeProcessor analyzeProcessor, final FileSearchEngine fileSearchEngine) {
        this.analyzeProcessor = analyzeProcessor;
        this.fileSearchEngine = fileSearchEngine;
    }

    public AnalysisDTO analyze(String path) {
        List<File> allFiles = fileSearchEngine.searchForFiles(path);
        List<File> srdFiles = fileSearchEngine.filterFiles(allFiles, srdFilePattern, false);
        List<File> sruFiles = fileSearchEngine.filterFiles(allFiles, sruFilePattern, false);

        Analysis analysis = analyzeProcessor.analyze(srdFiles, sruFiles);

        return new AnalysisDTO();
    }
}
