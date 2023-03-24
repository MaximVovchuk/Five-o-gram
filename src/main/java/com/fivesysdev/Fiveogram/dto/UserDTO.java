package com.fivesysdev.Fiveogram.dto;

import lombok.Builder;

@Builder
public record UserDTO(String name, String surname, String username, String password) {
}
