package com.os.islamicbank.pfwhelper.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ConnectionExt {

    private final Connection connection;
    private final File userObjectFile;
    private final List<String> dataObjectLines;

    public ConnectionExt(Connection connection, File userObjectFile) {
        this.connection = connection;
        this.userObjectFile = userObjectFile;
        this.dataObjectLines = new ArrayList<>();
    }

    public void addDataObjectLine(String line) {
        this.dataObjectLines.add(line);
    }
}
