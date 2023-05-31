package com.fivesysdev.Fiveogram.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@ApiModel(description = "The data transfer object for a post")
public record PostDTO(
        @ApiModelProperty(value = "The text content of the post")
        String text,
        @ApiModelProperty(value = "The list of attached files")
        List<MultipartFile> multipartFiles,
        @ApiModelProperty(value = "The ID of the sponsor (nullable)")
        @Nullable
        Long sponsorId) {
}
