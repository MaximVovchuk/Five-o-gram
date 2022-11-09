package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostReport {
    @JsonIgnoreProperties({"commentList","likesList"})
    private Post post;
    private List<String> reportTexts = new ArrayList<>();

    public void addReportText(Report report){
        reportTexts.add(report.getText());
    }
}
