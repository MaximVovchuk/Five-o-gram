package com.fivesysdev.Fiveogram.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record UserDTO(@NotNull String name, @NotNull String surname, @NotNull String username, @NotNull String email,
                      @NotNull String password) {
}
