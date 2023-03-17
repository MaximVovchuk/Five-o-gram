package com.fivesysdev.Fiveogram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostReportDTO {
    @JsonIgnoreProperties({"commentList", "likesList"})
    private Post post;
    private List<String> reportTexts = new ArrayList<>();

    public void addReportText(String text) {
        reportTexts.add(text);
    }

    public PostReportDTO(Post post) {
        this.post = post;
    }
}
