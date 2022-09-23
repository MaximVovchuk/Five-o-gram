package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status406UsernameBusyException;
import com.fivesysdev.Fiveogram.exceptions.Status407WrongPasswordException;
import com.fivesysdev.Fiveogram.services.LoginService;
import com.fivesysdev.Fiveogram.services.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authenticationDTO) throws UsernameNotFoundException, Status407WrongPasswordException {
        return loginService.login(authenticationDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPage(@RequestBody @Valid UserDTO userDTO,
                                          BindingResult bindingResult) throws Status406UsernameBusyException {
        return registrationService.register(userDTO, bindingResult);
    }
}
