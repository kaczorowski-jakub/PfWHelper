package com.os.islamicbank.pfwhelper;

import com.os.islamicbank.pfwhelper.core.AnalyzeFacade;
import com.os.islamicbank.pfwhelper.core.dto.Report;
import com.os.islamicbank.pfwhelper.core.report.ReportFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PfWHelperCmd implements CommandLineRunner {

    private final AnalyzeFacade analyzeFacade;
    private final ReportFacade reportFacade;

    @Autowired
    public PfWHelperCmd(final AnalyzeFacade analyzeFacade, ReportFacade reportFacade) {
        this.analyzeFacade = analyzeFacade;
        this.reportFacade =reportFacade;
    }

    public static void main(String[] args) {
        SpringApplication.run(PfWHelperCmd.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("PfWHelper Command Line Runner");
        if (args.length < 1) {
            log.error("No path argument given");
            return;
        }

        Report report = analyzeFacade.analyze(args[0]);
        reportFacade.print(report);
    }
}
