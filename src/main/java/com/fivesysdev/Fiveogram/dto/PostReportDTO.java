package com.fivesysdev.Fiveogram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public record PostReportDTO(@JsonIgnoreProperties({"commentList", "likesList"}) Post post, List<String> reportTexts) {

    public void addReportText(String text) {
        reportTexts.add(text);
    }

    public PostReportDTO(Post post) {
        this(post, new ArrayList<>());
    }
}
