package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO: 9/3/23 make sure if you need constructors in dtos and remove it if its not used anywhere
public class AuthenticationDTO {
    private String username;
    private String password;
}
