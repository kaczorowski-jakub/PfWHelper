package com.os.islamicbank.pfwhelper.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {
    private final LocalDate date;
    private final UUID uuid;
    private final List<ReportRecord> records;

    public Report() {
        date = LocalDate.now();
        uuid = UUID.randomUUID();
        records = new ArrayList<>();
    }

    public synchronized void addRecord(ReportRecord record) {
        records.add(record);
    }
}
