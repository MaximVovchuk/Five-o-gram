package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.config.MyUserDetails;
import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.services.AuthService;
import com.fivesysdev.Fiveogram.services.RegistrationService;
import com.fivesysdev.Fiveogram.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class StartController {
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;

    public StartController(JWTUtil jwtUtil, ModelMapper modelMapper, AuthService authService, UserValidator userValidator, PasswordEncoder passwordEncoder, RegistrationService registrationService) {
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authService = authService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                                    BindingResult bindingResult){
        MyUserDetails userDetails;
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(Map.of("Message","Bad credentials"), HttpStatus.BAD_REQUEST);
        }
        try {
            userDetails = authService.loadUserByUsername(authenticationDTO.getUsername());
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(Map.of("Message", "Wrong username"), HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(authenticationDTO.getPassword(),userDetails.getPassword())){
            return new ResponseEntity<>(Map.of("Message", "Wrong password"), HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token", token),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerPage(@RequestBody @Valid UserDTO userDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Map.of("Message", "Validation Error"),HttpStatus.BAD_REQUEST);
        }
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Map.of("Message", "This username is already taken"),HttpStatus.BAD_REQUEST);
        }
        registrationService.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token", token),HttpStatus.OK);
    }


    public User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }
}
