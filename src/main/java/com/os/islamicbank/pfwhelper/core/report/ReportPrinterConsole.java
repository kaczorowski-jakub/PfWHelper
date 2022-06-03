package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.dto.ReportNewUserObject;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("reportPrinterConsole")
class ReportPrinterConsole implements ReportPrinter {

    @Override
    public void print(Report report) {

        System.out.println("UUID: " + report.getUuid());
        System.out.println("Date: " + report.getDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        System.out.println("Root Path: " + report.getRootPath());
        System.out.println("-----");

        report.getNewDataWindows().forEach(reportRecordHeader -> {
            System.out.println("File: " + reportRecordHeader.getCustomDataWindowFile());
            System.out.println("Status: " + reportRecordHeader.getStatus());
            if (reportRecordHeader.hasObjects()) {
                printDetails(reportRecordHeader.getNewUserObjects());
            }
            System.out.println("---");
        });
    }

    private void printDetails(List<ReportNewUserObject> reportNewUserObjects) {
        reportNewUserObjects.forEach(reportNewUserObject -> {
            String tabs1 = getTabs(1);
            System.out.println(tabs1 + reportNewUserObject.getFile().getAbsolutePath());
            System.out.println(tabs1 + "[" + reportNewUserObject.getDoObject() + "]" + reportNewUserObject.getDoLine());
            System.out.println(tabs1 + "[" + reportNewUserObject.getGtObject() + "]" + reportNewUserObject.getGtLine());

            if (reportNewUserObject.hasObjects()) {
                reportNewUserObject.getOldUserObjects().forEach(oldUserObject -> {
                    String tabs2 = getTabs(2);
                    System.out.println(tabs2 + oldUserObject.getFile().getAbsolutePath());

                    if (oldUserObject.hasObjects()) {
                        oldUserObject.getOldDataWindows().forEach(oldDataWindow -> {
                            String tabs3 = getTabs(3);
                            System.out.println(tabs3 + "[" + oldDataWindow.getDoObject() + "]" + oldDataWindow.getDoLine());
                            if (oldDataWindow.hasFiles()) {
                                oldDataWindow.getFiles().forEach(file -> {
                                    String tabs4 = getTabs(4);
                                    System.out.println(tabs4 + file.getAbsolutePath());
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private String getTabs(int level) {
        String ret = "";
        for (int i = 0; i < level; i++) {
            ret += "\t";
        }
        return ret;
    }
}
