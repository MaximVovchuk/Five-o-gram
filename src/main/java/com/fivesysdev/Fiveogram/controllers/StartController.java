package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.exceptions.Status440WrongPasswordException;
import com.fivesysdev.Fiveogram.services.LoginService;
import com.fivesysdev.Fiveogram.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<String> login(@RequestBody AuthenticationDTO authenticationDTO) throws Status440WrongPasswordException {
        return ResponseEntity.ok(loginService.login(authenticationDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPage(@RequestBody @Valid UserDTO userDTO,
                                          BindingResult bindingResult) throws Status439UsernameBusyException {
        String token = registrationService.register(userDTO, bindingResult);
        return ResponseEntity.ok(token);
    }
}
