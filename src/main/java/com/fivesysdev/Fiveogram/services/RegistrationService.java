package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status406UsernameBusyException;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserValidator userValidator, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<String> register(UserDTO userDTO, BindingResult bindingResult) throws Status406UsernameBusyException {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new Status406UsernameBusyException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    public User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }
}
