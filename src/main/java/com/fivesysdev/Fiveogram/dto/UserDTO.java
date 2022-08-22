package com.fivesysdev.Fiveogram.dto;

import com.fivesysdev.Fiveogram.models.Picture;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
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
   // private Picture avatar;
}
