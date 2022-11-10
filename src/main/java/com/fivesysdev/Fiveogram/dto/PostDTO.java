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
//    @Nullable
//    private List<MarkDTO> markDTOs;
    private List<Integer> widths;
    private List<Integer> heights;
    private List<String> usernames;
    private List<Integer> photosCount;
    @Nullable
    private Long sponsorId;
}
