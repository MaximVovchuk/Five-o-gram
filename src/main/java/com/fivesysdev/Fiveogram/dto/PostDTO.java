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
// TODO: 9/3/23 make sure if you need constructors in dtos and remove it if its not used anywhere
public class PostDTO {
    public String text;
    public List<MultipartFile> multipartFiles;
    @Nullable
    public Long sponsorId;
}
