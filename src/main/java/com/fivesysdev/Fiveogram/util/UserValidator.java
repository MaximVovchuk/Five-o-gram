package com.fivesysdev.Fiveogram.util;

import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.services.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        System.out.println("Человек с такой логином уже существует");
        errors.rejectValue("username", "", "Человек с такой логином уже существует");
    }
}
