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
public class ReportOldUserObject {
    private File file;
    private List<ReportOldDataWindow> oldDataWindows;

    public void addOldDataWindow(ReportOldDataWindow oldDataWindow) {
        if (oldDataWindows == null) {
            oldDataWindows = new ArrayList<>();
        }
        oldDataWindows.add(oldDataWindow);
    }

    public boolean hasObjects() {
        return oldDataWindows == null || oldDataWindows.isEmpty() ? false : true;
    }
}
