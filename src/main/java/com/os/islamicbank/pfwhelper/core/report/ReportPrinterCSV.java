package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.dto.ReportNewDataWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("reportPrinterCSV")
class ReportPrinterCSV implements ReportPrinter {

    private final String[] HEADERS = {"zd_new", "polozenie zd_new", "zuo_new", "polozenie zuo_new", "uo_old", "polozenie uo_old", "d_old", "polozenie d_old", "Info", "Status"};
    @Value("${datawindow.file.extension}")
    private String datawindowFileExtension;
    @Value("${userobject.file.extension}")
    private String userobjectFileExtension;
    @Value("${csv.field.separator}")
    private String csvFiledSeparator;

    @Override
    public void print(Report report) {
        File outputCSV = new File("output.csv");
        try (FileWriter fileWriter = new FileWriter(outputCSV)) {
            fileWriter.write(prepareLine(HEADERS));
            for (ReportNewDataWindow reportNewDataWindow : report.getNewDataWindows()) {
                List<String> lines = prepareLines(reportNewDataWindow);
                for (String line : lines) {
                    fileWriter.write(line);
                }
            }
        } catch (IOException exception) {
            log.error("Error while creating an output file: " + exception.getMessage());
        }
    }

    private List<String> prepareLines(ReportNewDataWindow reportNewDataWindow) {

        List<String> lines = new ArrayList<>();
        String recordIncomplete = "Record incomplete";
        String status = reportNewDataWindow.getStatus().replace(csvFiledSeparator, "--->");

        // level 1
        String zdNew = getDataWindowName(reportNewDataWindow.getCustomDataWindowFile());
        String zdNewLoc = reportNewDataWindow.getCustomDataWindowFile().getAbsolutePath();

        if (!reportNewDataWindow.hasObjects()) {
            lines.add(prepareLine(zdNew, zdNewLoc, "", "", "", "", "", "", recordIncomplete, status));
            return lines;
        }
        // level 2
        reportNewDataWindow.getNewUserObjects().forEach(reportNewUserObject -> {
            String zuoNew = getUserObjectFilewName(reportNewUserObject.getFile());
            String zuoNewLoc = reportNewUserObject.getFile().getAbsolutePath();

            if (!reportNewUserObject.hasObjects()) {
                lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, "", "", "", "", recordIncomplete, status));
                return;
            }
            // level 3
            reportNewUserObject.getOldUserObjects().forEach(reportOldUserObject -> {
                String uoOld = getUserObjectFilewName(reportOldUserObject.getFile());
                String uoOldLoc = reportOldUserObject.getFile().getAbsolutePath();

                if (!reportOldUserObject.hasObjects()) {
                    lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, uoOldLoc, "", "", recordIncomplete, status));
                    return;
                }
                // level 4
                reportOldUserObject.getOldDataWindows().forEach(reportOldDataWindow -> {

                    if (!reportOldDataWindow.hasFiles()) {
                        lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, uoOldLoc, "", "", recordIncomplete, status));
                        return;
                    }
                    // level 5
                    reportOldDataWindow.getFiles().forEach(file -> {
                        String dOld = getDataWindowName(file);
                        String dOldLoc = file.getAbsolutePath();
                        lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, uoOldLoc, dOld, dOldLoc, "", ""));
                    });
                });
            });
        });
        return lines;
    }

    private String prepareLine(String... fields) {
        StringBuilder sb = new StringBuilder();
        Arrays.asList(fields).forEach(field -> sb.append(field).append(csvFiledSeparator));
        sb.append("\n");
        return sb.toString();
    }

    private String getDataWindowName(File customDataWindowFile) {
        return customDataWindowFile.getName().replace(datawindowFileExtension, "");
    }

    private String getUserObjectFilewName(File userObjectFile) {
        return userObjectFile.getName().replace(userobjectFileExtension, "");
    }

}
