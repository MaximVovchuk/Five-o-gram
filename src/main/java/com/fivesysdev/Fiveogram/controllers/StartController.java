package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.dto.RegisterConfirmationDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.exceptions.Status440WrongPasswordException;
import com.fivesysdev.Fiveogram.exceptions.Status455WrongCodeException;
import com.fivesysdev.Fiveogram.services.LoginService;
import com.fivesysdev.Fiveogram.services.RegistrationService;
import com.fivesysdev.Fiveogram.util.Response;
import jakarta.mail.MessagingException;
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
            throws Status440WrongPasswordException, MessagingException {
        return new Response<>(loginService.login(authenticationDTO));
    }

    @PostMapping("/register")
    public Response<String> baseRegister(@RequestBody UserDTO userDTO)
            throws Status439UsernameBusyException, MessagingException {
        return new Response<>(registrationService.baseRegister(userDTO));
    }

    @PostMapping("/register/confirm")
    public Response<String> confirmRegister(@RequestBody RegisterConfirmationDTO registerConfirmationDTO)
            throws Status455WrongCodeException {
        return new Response<>(registrationService.endRegister(registerConfirmationDTO.email(), registerConfirmationDTO.code()));
    }
}