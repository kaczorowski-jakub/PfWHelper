package com.os.islamicbank.pfwhelper.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Connection {
    private final File customDataWindowFile;
    private final File userObjectFile;
    private final List<String> dataObjectLines;
    private final Set<String> globalTypeLines;

    public Connection(File customDataWindowFile, File userObjectFile) {
        this.customDataWindowFile = customDataWindowFile;
        this.userObjectFile = userObjectFile;
        this.dataObjectLines = new ArrayList<>();
        this.globalTypeLines = new HashSet<>();
    }

    public void addDataObjectLine(String line) {
        this.dataObjectLines.add(line);
    }

    public void addGlobalTypeLine(String line) {
        this.globalTypeLines.add(line);
    }

    public boolean isCustomDataWindowFileFound() {
        return !this.dataObjectLines.isEmpty();
    }

    public File getCustomDataWindowFile() {
        return customDataWindowFile;
    }

    public File getUserObjectFile() {
        return userObjectFile;
    }

    public List<String> getDataObjectLines() {
        return dataObjectLines;
    }

    public Set<String> getGlobalTypeLines() {
        return globalTypeLines;
    }

    public String printConnection() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.customDataWindowFile.getAbsolutePath())
                .append(" found in file: ")
                .append(this.userObjectFile.getAbsolutePath());
        sb.append("\nat lines: ");
        for (String line : dataObjectLines) {
            sb.append("\n").append(line);
        }
        sb.append("\nglobal lines: ");
        for (String line : globalTypeLines) {
            sb.append("\n").append(line);
        }
        return sb.toString();
    }
}
