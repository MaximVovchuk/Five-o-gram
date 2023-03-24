package com.fivesysdev.Fiveogram.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record PostDTO (String text, List<MultipartFile> multipartFiles, @Nullable Long sponsorId){ }
