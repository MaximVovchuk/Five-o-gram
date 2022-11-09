package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data
public class PostReport extends Report {
    @JsonIgnoreProperties({"commentList","likesList"})
    private Post post;
}
