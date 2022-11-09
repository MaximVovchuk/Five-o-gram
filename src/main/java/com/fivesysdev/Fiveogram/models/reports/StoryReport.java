package com.fivesysdev.Fiveogram.models.reports;

import com.fivesysdev.Fiveogram.models.Story;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoryReport {
    private final Story story;
    List<String> reportTexts = new ArrayList<>();

    public void addReportText(String text) {
        reportTexts.add(text);
    }

    public StoryReport(Story story) {
        this.story = story;
    }
}
