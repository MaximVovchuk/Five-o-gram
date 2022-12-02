package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.exceptions.Status440WrongPasswordException;
import com.fivesysdev.Fiveogram.services.LoginService;
import com.fivesysdev.Fiveogram.services.RegistrationService;
import com.fivesysdev.Fiveogram.util.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class StartController {
    private final LoginService loginService;
    private final RegistrationService registrationService;

    public StartController(LoginService loginService, RegistrationService registrationService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public Response<String> login(@RequestBody AuthenticationDTO authenticationDTO)
            throws Status440WrongPasswordException {
        return new Response<>(loginService.login(authenticationDTO));
    }

    @PostMapping("/register")
    public Response<String> registerPage(@RequestBody UserDTO userDTO)
            throws Status439UsernameBusyException {
        return new Response<>(registrationService.register(userDTO));
    }
}