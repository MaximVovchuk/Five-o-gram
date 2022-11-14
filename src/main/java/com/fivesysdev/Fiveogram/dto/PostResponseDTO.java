package com.fivesysdev.Fiveogram.dto;

import com.fivesysdev.Fiveogram.models.Mark;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponseDTO {
    public Post post;
    public List<Mark> marks;
}
