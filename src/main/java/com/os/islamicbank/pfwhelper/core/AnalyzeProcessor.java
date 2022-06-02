package com.os.islamicbank.pfwhelper.core;

import com.os.islamicbank.pfwhelper.core.dto.Report;

import java.io.File;
import java.util.List;

interface AnalyzeProcessor {

     Report analyze(List<File> srdList, List<File> sruList, List<File> srdCustomList);

}
