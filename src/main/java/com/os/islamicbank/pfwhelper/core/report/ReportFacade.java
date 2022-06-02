package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportFacade {

    private final ReportPrinter reportPrinter;

    public ReportFacade(ReportPrinter reportPrinter) {
        this.reportPrinter = reportPrinter;
    }

    public void print(Report report) {
        reportPrinter.print(report);
    }

}
