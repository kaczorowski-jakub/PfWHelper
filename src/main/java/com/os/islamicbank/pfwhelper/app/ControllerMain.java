package com.os.islamicbank.pfwhelper.app;

import com.os.islamicbank.pfwhelper.core.AnalyzeFacade;
import org.springframework.beans.factory.annotation.Autowired;

public class ControllerMain {

    private AnalyzeFacade analyzeFacade;

    @Autowired
    public ControllerMain(final AnalyzeFacade analyzeFacade) {
        this.analyzeFacade = analyzeFacade;
    }

}
