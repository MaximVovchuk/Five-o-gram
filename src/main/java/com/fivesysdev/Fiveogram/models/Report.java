package com.fivesysdev.Fiveogram.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Report {
    private final List<String> reportTexts = new ArrayList<>();

    public void addReportText(ReportPostEntity report) {
        reportTexts.add(report.getText());
    }
}
