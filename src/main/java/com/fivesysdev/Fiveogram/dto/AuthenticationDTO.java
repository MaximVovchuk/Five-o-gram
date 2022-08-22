package com.fivesysdev.Fiveogram.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AuthenticationDTO {
    @NotEmpty(message = "username should not me empty")
    @Size(min = 6, max = 100, message = "username should be 6 to 100 characters long")
    private String username;
    @NotEmpty(message = "username should not me empty")
    @Size(min = 8, max = 100, message = "password should be 2 to 100 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
