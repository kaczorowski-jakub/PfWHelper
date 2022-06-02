package com.os.islamicbank.pfwhelper.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
class ScannerDispatcher {

    @Value("${threads}")
    private int threads;

    @Value("${dataobject.pattern}")
    private String dataobjectPattern;

    @Value("${globaltype.pattern}")
    private String globaltypePattern;

    List<UserObjectScanResult> run(List<File> fileList) {
        log.debug("scanner dispatcher run");

        int filesPerThread = (int) Math.ceil(((double) fileList.size()) / ((double) threads));
        int portion = 0;
        UserObjectScanner[] userObjectScanners = new UserObjectScanner[threads];
        List<UserObjectScanResult> results = new ArrayList<>();

        // run threads
        for (int i = 0; i < userObjectScanners.length; i++) {
            List<File> fileListPortion = getPortion(fileList, portion, filesPerThread);
            UserObjectScanner userObjectScanner = new UserObjectScanner(fileListPortion, dataobjectPattern, globaltypePattern);
            userObjectScanners[i] = userObjectScanner;
            userObjectScanner.start();
            portion++;
        }

        // collect results
        for (UserObjectScanner userObjectScanner : userObjectScanners) {
            try {
                userObjectScanner.join();
                results.addAll(userObjectScanner.getUserObjectScanResults());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        return results;
    }

    private List<File> getPortion(List<File> files, int portion, int filesPerThread) {
        int from = portion * filesPerThread;
        int to = ((portion + 1) * filesPerThread) >= files.size() ? files.size() : ((portion + 1) * filesPerThread);

        return files.subList(from, to);
    }

}
