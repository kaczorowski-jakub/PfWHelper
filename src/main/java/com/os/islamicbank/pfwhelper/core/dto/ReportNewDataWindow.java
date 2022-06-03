package com.os.islamicbank.pfwhelper.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
public class ReportNewDataWindow {
    private File customDataWindowFile;
    private List<ReportNewUserObject> newUserObjects;
    private String status;

    public void addNewUserObject(ReportNewUserObject newUserObject) {
        if (newUserObjects == null) {
            newUserObjects = new ArrayList<>();
        }
        newUserObjects.add(newUserObject);
    }

    public boolean hasObjects() {
        return newUserObjects == null || newUserObjects.isEmpty() ? false : true;
    }
}
