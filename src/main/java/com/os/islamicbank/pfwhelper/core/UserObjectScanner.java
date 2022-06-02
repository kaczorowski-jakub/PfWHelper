package com.os.islamicbank.pfwhelper.core;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class UserObjectScanner extends Thread {

    private final List<File> userObjectFiles;
    private final String dataobjectPattern;
    private final String globaltypePattern;
    private final List<UserObjectScanResult> userObjectScanResults;

    public UserObjectScanner(List<File> userObjectFiles, String dataobjectPattern, String globaltypePattern) {
        this.userObjectFiles = userObjectFiles;
        this.dataobjectPattern = dataobjectPattern;
        this.globaltypePattern = globaltypePattern;
        this.userObjectScanResults = new ArrayList<>();
    }

    @Override
    public void run() {
        log.debug("UserObjectScanner #" + Thread.currentThread().getName() + " started");
        for (File file : userObjectFiles) {
            log.debug("UserObjectScanner #" + Thread.currentThread().getName() + " is processing " + file.getAbsolutePath());
            UserObjectScanResult userObjectScanResult = scanFile(file);
            userObjectScanResults.add(userObjectScanResult);
            log.debug("UserObjectScanner #" + Thread.currentThread().getName() + " is done " + file.getAbsolutePath());
        }
        log.debug("UserObjectScanner #" + Thread.currentThread().getName() + " finished");
    }

    private UserObjectScanResult scanFile(File userObjectFile) {

        UserObjectScanResult userObjectScanResult = new UserObjectScanResult(userObjectFile);
        try (Scanner reader = new Scanner(userObjectFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.matches(dataobjectPattern)) {
                    userObjectScanResult.addLine(line, UserObjectScanResult.LineType.DO);
                }
                if (line.matches(globaltypePattern)) {
                    userObjectScanResult.addLine(line, UserObjectScanResult.LineType.GT);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("UserObjectScanner #" + Thread.currentThread().getName() + " File not found: " + userObjectFile.getAbsolutePath());
        }

        return userObjectScanResult;
    }

    public List<UserObjectScanResult> getUserObjectScanResults() {
        return userObjectScanResults;
    }
}
