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
    private final List<ReportNewDataWindow> newDataWindows;
    private String rootPath;

    public Report() {
        date = LocalDate.now();
        uuid = UUID.randomUUID();
        newDataWindows = new ArrayList<>();
    }

    public synchronized void addNewDataWindow(ReportNewDataWindow newDataWindow) {
        newDataWindows.add(newDataWindow);
    }

    public List<ReportNewDataWindow> getNewDataWindows() {
        return this.newDataWindows;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
