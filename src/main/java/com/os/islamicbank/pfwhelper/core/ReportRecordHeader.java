package com.os.islamicbank.pfwhelper.core;

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
}
