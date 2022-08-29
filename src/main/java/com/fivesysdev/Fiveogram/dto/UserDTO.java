package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "this should not be empty")
    @Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
    private String name;
    @NotBlank(message = "this should not be empty")
    @Size(min = 2, max = 32, message = "surname must be between 2 and 32 characters long")
    private String surname;
    @NotBlank(message = "this should not be empty")
    @Size(min = 6, max = 32, message = "username must be between 6 and 32 characters long")
    private String username;
    @NotBlank(message = "this should not be empty")
    @Length(min = 8, message = "password should be at least 8 characters")
    private String password;
}
