package com.os.islamicbank.pfwhelper.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Builder
@Setter
@Getter
public class ReportRecordHeader {
    private File customDataWindowFile;
    private ReportRecordDetail detail;
    private String status;
    private int findings;

    public void incFindings() {
        findings++;
    }
}
