package com.os.islamicbank.pfwhelper.core;

import java.io.File;
import java.util.List;

interface AnalyzeProcessor {

     Analysis analyze(List<File> srdList, List<File> sruList, List<File> srdCustomList);

}
