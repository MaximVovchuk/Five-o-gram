package com.fivesysdev.Fiveogram.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class PostDTO {
    public String text;
    public List<MultipartFile> multipartFiles;
    @Nullable
    public Long sponsorId;
}
