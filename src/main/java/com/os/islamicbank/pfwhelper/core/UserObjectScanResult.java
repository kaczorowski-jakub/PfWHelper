package com.os.islamicbank.pfwhelper.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class UserObjectScanResult {
    private final File userObjectFile;
    private final List<Line> lines;

    public UserObjectScanResult(File userObjectFile) {
        this.userObjectFile = userObjectFile;
        this.lines = new ArrayList<>();
    }

    public void addLine(String lineTxt, LineType lineType) {

        String value = null;
        switch (lineType) {
            case DO:
                value = retrieveDataObject(lineTxt);
                break;
            case GT:
                value = retrieveParent(lineTxt);
                break;
        }
        Line line = new Line(lineTxt, value, lineType);
        lines.add(line);

    }

    public File getUserObjectFile() {
        return userObjectFile;
    }

    public List<Line> getLines() {
        return lines;
    }

    private String retrieveDataObject(String line) {
        int fromIdx = line.indexOf("\"");
        int toIdx = line.lastIndexOf("\"");
        try {
            if (fromIdx > -1 && toIdx > -1 && (fromIdx + 1 != toIdx)) {
                return line.substring(fromIdx + 1, toIdx).trim();
            } else{
                return "";
            }
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    private String retrieveParent(String line) {
        String searchItem = "from ";
        int fromIdx = line.lastIndexOf(searchItem);
        if (fromIdx > -1) {
            return line.substring(fromIdx + searchItem.length()).trim();
        } else {
            return "";
        }
    }

    enum LineType {
        DO, GT
    }

    @AllArgsConstructor
    @Getter
    class Line {
        private String line;
        private String value;
        private LineType type;
    }
}
