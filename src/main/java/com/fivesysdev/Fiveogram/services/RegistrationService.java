package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.exceptions.Status455WrongCodeException;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.UserToRegister;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.repositories.UserToRegisterRepository;
import com.fivesysdev.Fiveogram.roles.Role;
import com.fivesysdev.Fiveogram.serviceInterfaces.MailSenderService;
import jakarta.mail.MessagingException;
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
    private final UserToRegisterRepository userToRegisterRepository;
    private final MailSenderService mailSenderService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    public String baseRegister(UserDTO userDTO) throws Status439UsernameBusyException, MessagingException {
        UserToRegister user = convertToUser(userDTO);
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userToRegisterRepository.save(user);
        mailSenderService.sendMessage("Registration", "Your code is " + user.getCode(), user.getEmail());
        return user.getEmail();
    }

    public String endRegister(String email, int code) throws Status455WrongCodeException {
        UserToRegister user = userToRegisterRepository.findByEmail(email);
        if (user.getCode() == code) {
            User user1 = modelMapper.map(user, User.class);
            user1.setRole(Role.USER);
            userRepository.save(user1);
            userToRegisterRepository.delete(user);
            return jwtUtil.generateToken(user1.getUsername(), List.of(Role.USER));
        }
        throw new Status455WrongCodeException();
    }

    private void validate(UserToRegister user) throws Status439UsernameBusyException {
        User fromDB = userRepository.findUserByUsername(user.getUsername());
        if (fromDB != null) {
            throw new Status439UsernameBusyException();
        }
        if (user.getUsername().isBlank() || user.getUsername().isBlank()) {
            throw new IllegalArgumentException();
        }
        if (user.getPassword().isBlank() || user.getPassword().isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    private UserToRegister convertToUser(UserDTO userDTO) {
        UserToRegister user = new UserToRegister();
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setName(userDTO.name());
        user.setSurname(userDTO.surname());
        user.setEmail(userDTO.email());
        user.setCode(generateCode());
        return user;
    }

    private int generateCode() {
        return (int) (Math.random() * 10000);
    }
}
