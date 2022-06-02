package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.dto.ReportRecordDetail;
import com.os.islamicbank.pfwhelper.core.dto.ReportRecordHeader;
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

    private final String[] HEADERS = {"zd_new", "położenie zd_new", "zuo_new", "położenie zuo_new", "uo_old", "położenie uo_old", "d_old", "położenie d_old", "Info", "Status"};
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
            for (ReportRecordHeader reportRecordHeader : report.getRecords()) {
                List<String> lines = prepareLines(reportRecordHeader);
                for (String line : lines) {
                    fileWriter.write(line);
                }
            }
        } catch (IOException exception) {
            log.error("Error while creating an output file: " + exception.getMessage());
        }
    }

    private List<String> prepareLines(ReportRecordHeader reportRecordHeader) {

        List<String> lines = new ArrayList<>();
        String recordIncomplete = "Record incomplete";
        String status = reportRecordHeader.getStatus().replace(csvFiledSeparator, "--->");

        // level 1
        String zdNew = getDataWindowName(reportRecordHeader.getCustomDataWindowFile());
        String zdNewLoc = reportRecordHeader.getCustomDataWindowFile().getAbsolutePath();

        // level 2
        ReportRecordDetail level2 = reportRecordHeader.getDetail();
        if (level2 == null) {
            lines.add(prepareLine(zdNew, zdNewLoc, "", "", "", "", "", "", recordIncomplete, status));
            return lines;
        }
        String zuoNew = getUserObjectFilewName(level2.getFile());
        String zuoNewLoc = level2.getFile().getAbsolutePath();

        // level 3
        ReportRecordDetail level3 = level2.getDetail();
        if (level3 == null) {
            lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, "", "", "", "", recordIncomplete, status));
            return lines;
        }
        String uoOld = level3.getObject();

        // level 4
        ReportRecordDetail level4 = level3.getDetail();
        if (level4 == null) {
            lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, "", "", "", recordIncomplete, status));
            return lines;
        }
        String uoOldLoc = level4.getFile().getAbsolutePath();
        String dOld = level4.getObject();

        // level 5
        List<File> foundFiles = level4.getFoundFiles();
        if (foundFiles == null || foundFiles.isEmpty()) {
            lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, uoOldLoc, dOld, "", recordIncomplete, status));
            return lines;
        }
        foundFiles.forEach(foundFile -> {
            lines.add(prepareLine(zdNew, zdNewLoc, zuoNew, zuoNewLoc, uoOld, uoOldLoc, dOld, foundFile.getAbsolutePath(), "", ""));
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
