package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record AuthenticationDTO(String username, String password) {
}
