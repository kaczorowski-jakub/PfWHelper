package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.dto.ReportRecordDetail;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportPrinterConsole implements ReportPrinter {

    @Override
    public void print(Report report) {

        System.out.println("UUID: " +report.getUuid());
        System.out.println("Date: " + report.getDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        System.out.println("Root Path: " + report.getRootPath());
        System.out.println("-----");

        report.getRecords().forEach(reportRecordHeader -> {
            System.out.println("File: " + reportRecordHeader.getCustomDataWindowFile());
            System.out.println("No. of matches: " + reportRecordHeader.getFindings());
            System.out.println("Status: " + reportRecordHeader.getStatus());
            printDetails(reportRecordHeader.getDetail(), 1);
            System.out.println("---");
        });
    }

    private void printDetails(ReportRecordDetail detail, int level) {
        if (detail != null) {
            String tabs = getTabs(level);
            System.out.println(tabs + detail.getFile().getAbsolutePath());
            System.out.println(tabs + detail.getLine());
            System.out.println(tabs + detail.getObject());
            printFoundFiles(detail.getFoundFiles(), ++level);
            printDetails(detail.getDetail(), ++level);
        }
    }

    private void printFoundFiles(List<File> files, int level) {
        String tabs = getTabs(level);
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                System.out.println(tabs + file.getAbsolutePath());
            });
        }
    }

    private String getTabs(int level) {
        String ret = "";
        for (int i = 0 ; i < level ; i++) {
            ret+="\t";
        }
        return ret;
    }
}
