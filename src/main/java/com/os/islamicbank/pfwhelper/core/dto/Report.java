package com.os.islamicbank.pfwhelper.core.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Report {
    private final LocalDate date;
    private final UUID uuid;
    private final List<ReportRecordHeader> records;

    public Report() {
        date = LocalDate.now();
        uuid = UUID.randomUUID();
        records = new ArrayList<>();
    }

    public synchronized void addRecord(ReportRecordHeader record) {
        records.add(record);
    }

    public List<ReportRecordHeader> getRecords() {
        return records;
    }
}