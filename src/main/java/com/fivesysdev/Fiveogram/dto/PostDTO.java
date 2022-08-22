package com.fivesysdev.Fiveogram.dto;

import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private User author;
    private String text;
    private Picture pic;
    private User sponsor;
}
