package com.os.islamicbank.pfwhelper;

import com.os.islamicbank.pfwhelper.core.AnalyzeFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PfWHelperCmd implements CommandLineRunner {

    private final AnalyzeFacade analyzeFacade;

    @Autowired
    public PfWHelperCmd(final AnalyzeFacade analyzeFacade) {
        this.analyzeFacade = analyzeFacade;
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

        analyzeFacade.analyze(args[0]);
    }
}
