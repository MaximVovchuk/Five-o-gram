package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.roles.Role;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    public String register(UserDTO userDTO) throws Status439UsernameBusyException {
        User user = convertToUser(userDTO);
        user.setRole(Role.USER);
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtUtil.generateToken(user.getUsername(), List.of(Role.USER));
    }

    private void validate(User user) throws Status439UsernameBusyException {
        User fromDB = userRepository.findUserByUsername(user.getUsername());
        if (fromDB != null) {
            throw new Status439UsernameBusyException();
        }
        if(user.getPassword().isBlank() || user.getUsername().isBlank()){
            throw new IllegalArgumentException();
        }
    }

    private User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }
}
