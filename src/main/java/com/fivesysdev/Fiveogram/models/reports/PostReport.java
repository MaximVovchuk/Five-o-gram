package com.fivesysdev.Fiveogram.models.reports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PostReport {
    @JsonIgnoreProperties({"commentList","likesList"})
    private Post post;
    List<String> reportTexts = new ArrayList<>();
    public void addReportText(String text){
        reportTexts.add(text);
    }

    public PostReport(Post post) {
        this.post = post;
    }
}
