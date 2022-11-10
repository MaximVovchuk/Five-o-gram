package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String text;
    private List<MultipartFile> multipartFiles;
    @Nullable
    private List<Integer> widths;
    @Nullable
    private List<Integer> heights;
    @Nullable
    private List<String> usernames;
    @Nullable
    private List<Integer> photosCount;
    @Nullable
    private Long sponsorId;
}
