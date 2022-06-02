package com.os.islamicbank.pfwhelper.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
public class ReportRecordDetail {
    private File file;
    private String line;
    private String object;
    private ReportRecordDetail detail;
    private List<File> foundFiles;

    public void addFile(File file) {
        if (foundFiles == null) {
            foundFiles = new ArrayList<>();
        }
        this.foundFiles.add(file);
    }
}
