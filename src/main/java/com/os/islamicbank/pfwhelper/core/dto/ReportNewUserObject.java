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
public class ReportNewUserObject {
    private File file;
    private String doLine;
    private String doObject;
    private String gtLine;
    private String gtObject;
    private List<ReportOldUserObject> oldUserObjects;

    public void addOldUserObject(ReportOldUserObject oldUserObject) {
        if (oldUserObjects == null) {
            oldUserObjects = new ArrayList<>();
        }
        oldUserObjects.add(oldUserObject);
    }

    public boolean hasObjects() {
        return oldUserObjects == null || oldUserObjects.isEmpty() ? false : true;
    }

}
