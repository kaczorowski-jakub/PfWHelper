package com.os.islamicbank.pfwhelper.core;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.dto.ReportRecordDetail;
import com.os.islamicbank.pfwhelper.core.dto.ReportRecordHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
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
        if (reconciliation(userObjectFiles, results)) {
            processResults(results, customDataWindowFiles, dataWindowFiles, report);
        } else {
            log.error("There is a problem with reconciliation - please review the logs for missing files");
        }

        report.getRecords().stream().forEach(header -> {
            if (header.getFindings() > 0) {
                log.info("An object has been found: " + header.getCustomDataWindowFile());
            }
        });

        return report;
    }

    private boolean reconciliation(List<File> userObjectFiles, List<UserObjectScanResult> results) {
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
        return true;
    }

    private void processResults(List<UserObjectScanResult> results, List<File> customDataWindowFiles, List<File> dataWindowFiles, Report report) {
        customDataWindowFiles.forEach(customDataWindowFile -> {
            String customDataWindow = customDataWindowFile.getName().replace(datawindowFileExtension, "");
            List<UserObjectScanResult> findings = findResultByDataWindow(results, customDataWindow);

            if (findings.size() == 0) {
                report.addRecord(ReportRecordHeader.builder()
                        .status("no related *.sru file found")
                        .customDataWindowFile(customDataWindowFile)
                        .build());
            } else {
                ReportRecordHeader main = ReportRecordHeader.builder()
                        .status("found related *.sru file(s)")
                        .customDataWindowFile(customDataWindowFile)
                        .build();
                report.addRecord(main);

                findings.forEach(finding -> {
                    UserObjectScanResult.Line line1 = finding.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.DO).findFirst().get();
                    ReportRecordDetail rhd1 = ReportRecordDetail.builder()
                            .file(finding.getUserObjectFile())
                            .line(line1.getLine())
                            .object(line1.getValue())
                            .build();
                    main.setDetail(rhd1);

                    UserObjectScanResult.Line line2 = finding.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.GT).findFirst().get();
                    ReportRecordDetail rhd2 = ReportRecordDetail.builder()
                            .file(finding.getUserObjectFile())
                            .line(line2.getLine())
                            .object(line2.getValue())
                            .build();
                    rhd1.setDetail(rhd2);

                    results.stream().filter(result -> result.getUserObjectFile().getName().equals(line2.getValue() + userobjectFileExtension)).forEach(result -> {
                        Optional<UserObjectScanResult.Line> line3 = result.getLines().stream().filter(line -> line.getType() == UserObjectScanResult.LineType.DO).findFirst();

                        ReportRecordDetail rhd3 = ReportRecordDetail.builder()
                                .file(result.getUserObjectFile())
                                .line(line3.isPresent() ? line3.get().getLine() : "<<no dataobject>>")
                                .object(line3.isPresent() ? line3.get().getValue() : "<<no dataobject>>")
                                .build();
                        rhd2.setDetail(rhd3);

                        if (line3.isPresent()) {
                            dataWindowFiles.stream()
                                    .filter(customDataWindowFileSub -> customDataWindowFileSub.getName().equals(line3.get().getValue() + datawindowFileExtension))
                                    .forEach(customDataWindowFileSub -> {
                                        rhd3.addFile(customDataWindowFileSub);
                                        main.incFindings();
                                    });
                        }
                    });
                    if (rhd2.getDetail() == null) {
                        main.setStatus(main.getStatus() + "; " + "could not find " + line2.getValue() + ".sru file");
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
