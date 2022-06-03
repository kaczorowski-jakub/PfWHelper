package com.os.islamicbank.pfwhelper.core.report;

import com.os.islamicbank.pfwhelper.core.dto.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ReportFacade {

    private final ReportPrinter reportPrinter;

    @Autowired
    public ReportFacade(@Qualifier("reportPrinterCSV") ReportPrinter reportPrinter) {
    //public ReportFacade(@Qualifier("reportPrinterConsole") ReportPrinter reportPrinter) {
        this.reportPrinter = reportPrinter;
    }

    public void print(Report report) {
        reportPrinter.print(report);
    }

}
