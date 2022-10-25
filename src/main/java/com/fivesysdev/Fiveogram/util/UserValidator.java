package com.fivesysdev.Fiveogram.util;

import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.services.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class UserValidator implements Validator {
    private final AuthService authService;

    public UserValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            authService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("username", "", "This username is already taken");
    }
}
