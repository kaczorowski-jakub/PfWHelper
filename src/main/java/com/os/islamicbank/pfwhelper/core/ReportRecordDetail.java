package com.os.islamicbank.pfwhelper.core;

import lombok.Builder;
import lombok.Setter;

import java.io.File;

@Builder
@Setter
public class ReportRecordDetail {
    private File file;
    private String line;
    private String object;
    private ReportRecordDetail detail;
}
