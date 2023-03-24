package com.fivesysdev.Fiveogram.dto;

import com.fivesysdev.Fiveogram.models.Story;

import java.util.ArrayList;
import java.util.List;

public record StoryReportDTO(Story story, List<String> reportTexts) {
    public void addReportText(String text) {
        reportTexts.add(text);
    }

    public StoryReportDTO(Story story) {
        this(story, new ArrayList<>());
    }
}
