package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record MarksToAddDTO(List<MarkDTO> markDTOs) {
}
