package com.os.islamicbank.pfwhelper.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Slf4j
class AnalyzeProcessorImpl implements AnalyzeProcessor {

    private FileSearchEngine fileSearchEngine;

    @Value("${dataobject.pattern.prefix}")
    private String dataobjectPatternPrefix;

    @Value("${dataobject.pattern.postfix}")
    private String dataobjectPatternPostfix;

    @Value("${globaltype.pattern.prefix}")
    private String globaltypePatternPrefix;

    @Value("${globaltype.pattern.postfix}")
    private String globaltypePatternPostfix;

    @Value("${datawindow.file.extension}")
    private String dataWindowFileExtension;
    @Autowired
    private ScannerDispatcher scannerDispatcher;

    @Autowired
    public AnalyzeProcessorImpl(final FileSearchEngine fileSearchEngine) {
        this.fileSearchEngine = fileSearchEngine;
    }

    @Override
    public Report analyze(List<File> dataWindowFiles, List<File> userObjectFiles, List<File> customDataWindowFiles) {
        Report report = new Report();

        List<UserObjectScanResult> results = scannerDispatcher.run(userObjectFiles);
        reconciliation(userObjectFiles, results);
        processResults(results, customDataWindowFiles, userObjectFiles, report);

        return report;
        /*
        Report report = new Report();

        for (File customDataWindowFile : customDataWindowFiles) {
            // here is a place to split into threads

            ReportRecord reportRecord = null;
            List<Connection> connections = findDataWindowInUserObjects(userObjectFiles, customDataWindowFile);
            if (connections.size() > 0) {
                processConnections(connections, userObjectFiles);
                log.info("Connection(s) found for " + customDataWindowFile.getAbsolutePath());
            } else {
                reportRecord = ReportRecord.builder()
                        .customDataWindowFile(customDataWindowFile)
                        .connections(null)
                        .build();
                log.info("No Connections found for " + customDataWindowFile.getAbsolutePath());
            }
            report.addRecord(reportRecord);
        }
        return report;*/
    }

    private void reconciliation(List<File> userObjectFiles, List<UserObjectScanResult> results) {
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
            }
        }
    }

    private void processResults(List<UserObjectScanResult> results, List<File> customDataWindowFiles, List<File> userObjectFiles, Report report) {
        customDataWindowFiles.forEach(customDataWindowFile -> {
            String customDataWindow = customDataWindowFile.getName().replace(dataWindowFileExtension, "");
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

                    results.stream().filter(result -> {

                    });
                });
            }
        });
    }

    private List<UserObjectScanResult> findResultByDataWindow(List<UserObjectScanResult> results, String value) {
        return results.stream().filter(result -> {
            return result.getLines().stream().anyMatch(line -> line.getType() == UserObjectScanResult.LineType.DO && line.getValue().equals(value));
        }).collect(Collectors.toList());
    }

    private List<ConnectionExt> processConnections(List<Connection> connections, List<File> userObjectFiles) {

        List<ConnectionExt> connectionExts = new ArrayList<>();

        connections.forEach(connection -> {
            connection.getGlobalTypeLines().forEach(globalTypeLine -> {
                String userObjectFileName = retrieveParent(globalTypeLine, ".sru");
                File userObjectFile = findFile(userObjectFiles, userObjectFileName);
                ConnectionExt connectionExt = new ConnectionExt(connection, userObjectFile);
                if (userObjectFile == null) {
                    log.error("cannot find connected file: " + userObjectFileName);
                } else {
                    processUserObjectFile(userObjectFile, connectionExt);
                }
                connectionExts.add(connectionExt);
            });
        });

        return connectionExts;
    }

    private void processUserObjectFile(File userObjectFile, ConnectionExt connectionExt) {
        try (Scanner reader = new Scanner(userObjectFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.matches(dataobjectPatternPrefix + ".*")) {
                    connectionExt.addDataObjectLine(line);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("[processUserObjectFile] File not found: " + userObjectFile.getAbsolutePath());
        }
    }

    private File findFile(List<File> fileList, String fileName) {
        for (File file : fileList) {
            if (fileName.equals(file.getName())) {
                return file;
            }
        }

        return null;
    }

    private List<Connection> findDataWindowInUserObjects(List<File> userObjectFiles, File customDataWindowFile) {

        List<Connection> connections = new ArrayList<>();
        userObjectFiles.forEach(userObjectFile -> {
            Connection connection = findDataWindowInUserObject(userObjectFile, customDataWindowFile);
            if (connection.isCustomDataWindowFileFound()) {
                connections.add(connection);
            }
        });

        return connections;
    }

    private Connection findDataWindowInUserObject(File userObjectFile, File customDataWindowFile) {
        String searchedCustomDataWindowPattern = dataobjectPatternPrefix + fileToObjectName(customDataWindowFile) + dataobjectPatternPostfix;
        String searchedUserObjectPattern = globaltypePatternPrefix + fileToObjectName(userObjectFile) + globaltypePatternPostfix;

        Connection connection = new Connection(customDataWindowFile, userObjectFile);

        try (Scanner reader = new Scanner(userObjectFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.matches(searchedCustomDataWindowPattern)) {
                    connection.addDataObjectLine(line);
                    log.debug(customDataWindowFile.getName() + " ---> " + userObjectFile.getName() + " ---> " + line);
                }
                if (line.matches(searchedUserObjectPattern)) {
                    connection.addGlobalTypeLine(line);
                    log.debug(customDataWindowFile.getName() + " ---> " + userObjectFile.getName() + " ---> " + line);
                }

            }
        } catch (FileNotFoundException e) {
            log.error("[findDataWindowInUserObject] File not found: " + userObjectFile.getAbsolutePath());
        }
        return connection;
    }

    private String fileToObjectName(File file) {
        return file.getName().substring(0, file.getName().indexOf("."));
    }

    private String retrieveParent(String globalTypeLine, String fileExtension) {
        String searchItem = "from ";
        int fromIdx = globalTypeLine.lastIndexOf(searchItem);
        if (fromIdx > -1) {
            return globalTypeLine.substring(fromIdx + searchItem.length()).trim() + fileExtension;
        } else {
            return "";
        }
    }

}
