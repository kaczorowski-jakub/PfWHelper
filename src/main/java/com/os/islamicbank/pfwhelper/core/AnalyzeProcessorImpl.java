package com.os.islamicbank.pfwhelper.core;

import com.os.islamicbank.pfwhelper.core.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
class AnalyzeProcessorImpl implements AnalyzeProcessor {

    @Value("${datawindow.file.extension}")
    private String datawindowFileExtension;

    @Value("${userobject.file.extension}")
    private String userobjectFileExtension;

    private ScannerDispatcher scannerDispatcher;

    @Autowired
    public AnalyzeProcessorImpl(final ScannerDispatcher scannerDispatcher) {
        this.scannerDispatcher = scannerDispatcher;
    }

    @Override
    public Report analyze(List<File> dataWindowFiles, List<File> userObjectFiles, List<File> customDataWindowFiles) {
        Report report = new Report();

        List<UserObjectScanResult> results = scannerDispatcher.run(userObjectFiles);
        if (reconciliationScanning(userObjectFiles, results)) {
            processResults(results, customDataWindowFiles, dataWindowFiles, report);
            if (reconciliationProcessing(customDataWindowFiles, report)) {
                report.getNewDataWindows().stream().forEach(newDataWindow -> {
                    if (newDataWindow.hasObjects()) {
                        log.info("An object has been found: " + newDataWindow.getCustomDataWindowFile());
                    }
                });
            } else {
                log.error("There is a problem with reconciliation at processing level - please review the logs for missing *.srd files");
            }
        } else {
            log.error("There is a problem with reconciliation at scanning level - please review the logs for missing *.sru files");
        }

        return report;
    }

    private boolean reconciliationScanning(List<File> userObjectFiles, List<UserObjectScanResult> results) {
        for (File file : userObjectFiles) {
            boolean found = false;
            for (UserObjectScanResult result : results) {
                if (result.getUserObjectFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("File: " + file.getAbsolutePath() + "not processed!!!");
                return false;
            }
        }

        if (userObjectFiles.size() != results.size()) {
            log.error("The number of user object files is not equal the number of results");
            return false;
        }
        /*
        // GT line check
        results.forEach(result -> {
            Map<String, Integer> map = new HashMap<>();
            result.getLines().stream().filter(line -> line.getType()== UserObjectScanResult.LineType.GT).forEach(line -> {
                if (!map.containsKey(line.getLine())) {
                    map.put(line.getLine(),0);
                } else {
                    int i = map.get(line.getLine()) + 1;
                    map.put(line.getLine(), i);
                }
            });
            map.forEach((key, value) -> {
                if (value > 1) {
                    System.out.println(result.getUserObjectFile().getAbsolutePath() + " ---> " + key);
                }
            });
        });
         */
        return true;
    }

    private boolean reconciliationProcessing(List<File> customDataWindowFiles, Report report) {
        for (File file : customDataWindowFiles) {
            boolean found = false;
            for (ReportNewDataWindow newDataWindow : report.getNewDataWindows()) {
                if (newDataWindow.getCustomDataWindowFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("File: " + file.getAbsolutePath() + "not processed!!!");
                return false;
            }
        }


        if (customDataWindowFiles.size() != report.getNewDataWindows().size()) {
            log.error("The number of custom data window files is not equal the number of reported items");
            return false;
        }
        return true;
    }

    private void processResults(List<UserObjectScanResult> results, List<File> customDataWindowFiles, List<File> dataWindowFiles, Report report) {
        customDataWindowFiles.forEach(customDataWindowFile -> {
            String customDataWindow = customDataWindowFile.getName().replace(datawindowFileExtension, "");
            List<UserObjectScanResult> findings = findResultByDataWindow(results, customDataWindow);

            if (findings.size() == 0) {
                report.addNewDataWindow(ReportNewDataWindow.builder()
                        .status("no related *.sru file found")
                        .customDataWindowFile(customDataWindowFile)
                        .build());
            } else {
                ReportNewDataWindow reportNewDataWindow = ReportNewDataWindow.builder()
                        .status("found related *.sru file(s)")
                        .customDataWindowFile(customDataWindowFile)
                        .build();
                report.addNewDataWindow(reportNewDataWindow);

                // search for new user objects
                findings.forEach(finding -> {
                    // it doesn't matter how many times the same dataobject="xxx" line appears in one *.sru file, it matters that it appears at least once
                    UserObjectScanResult.Line doLine = finding.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.DO).findFirst().get();

                    // search for global type * from lines - we can assume that the lines will have the same content but can occur 2 times
                    UserObjectScanResult.Line gtLine = finding.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.GT).findFirst().get();

                    ReportNewUserObject newUserObject = ReportNewUserObject.builder()
                            .file(finding.getUserObjectFile())
                            .doLine(doLine.getLine())
                            .doObject(doLine.getValue())
                            .gtLine(gtLine.getLine())
                            .gtObject(gtLine.getValue())
                            .build();
                    reportNewDataWindow.addNewUserObject(newUserObject);

                    // old user object search
                    results.stream().filter(result -> result.getUserObjectFile().getName().equals(gtLine.getValue() + userobjectFileExtension)).forEach(result -> {

                        ReportOldUserObject reportOldUserObject = ReportOldUserObject.builder()
                                .file(result.getUserObjectFile())
                                .build();
                        newUserObject.addOldUserObject(reportOldUserObject);

                        result.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.DO).forEach(line -> {
                            ReportOldDataWindow reportOldDataWindow = ReportOldDataWindow.builder()
                                    .doLine(line.getLine())
                                    .doObject(line.getValue())
                                    .build();
                            reportOldUserObject.addOldDataWindow(reportOldDataWindow);

                            // old data window search
                            dataWindowFiles.stream()
                                    .filter(file -> file.getName().equals(line.getValue() + datawindowFileExtension))
                                    .forEach(file -> {
                                        reportOldDataWindow.addFile(file);
                                    });
                            if (!reportOldDataWindow.hasFiles()) {
                                reportNewDataWindow.setStatus(reportNewDataWindow.getStatus() + " ---> no files found for " + line.getValue());
                            }
                        });

                        if (!reportOldUserObject.hasObjects()) {
                            reportNewDataWindow.setStatus(reportNewDataWindow.getStatus() + " ---> no dataobjects defined in " + result.getUserObjectFile().getAbsolutePath());
                        }

                    });
                    if (!newUserObject.hasObjects()) {
                        reportNewDataWindow.setStatus(reportNewDataWindow.getStatus() + " ---> could not find " + gtLine.getValue() + ".sru file");
                    }
                });
            }
        });
    }

    private List<UserObjectScanResult> findResultByDataWindow(List<UserObjectScanResult> results, String value) {
        return results.stream().filter(result -> {
            return result.getLines().stream().anyMatch(line -> line.getType() == UserObjectScanResult.LineType.DO && line.getValue().equals(value));
        }).collect(Collectors.toList());
    }
}
