package com.os.islamicbank.pfwhelper.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class ReportOldDataWindow {
    private String doLine;
    private String doObject;
    private List<File> files;

    public void addFile(File file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(file);
    }

    public boolean hasFiles() {
        return files == null || files.isEmpty() ? false : true;
    }
}
