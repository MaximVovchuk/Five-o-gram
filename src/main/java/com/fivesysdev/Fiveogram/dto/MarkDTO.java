package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
public record MarkDTO(int height, int width, String username, long photoId) {
}
