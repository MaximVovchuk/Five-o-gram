package com.fivesysdev.Fiveogram.dto;

import com.fivesysdev.Fiveogram.models.Story;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoryReportDTO {
    private final Story story;
    List<String> reportTexts = new ArrayList<>();

    public void addReportText(String text) {
        reportTexts.add(text);
    }

    public StoryReportDTO(Story story) {
        this.story = story;
    }
}
