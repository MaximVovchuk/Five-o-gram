package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.config.JwtUser;
import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.exceptions.Status440WrongPasswordException;
import com.fivesysdev.Fiveogram.roles.Role;
import com.fivesysdev.Fiveogram.serviceInterfaces.MailSenderService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LoginService {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public String login(AuthenticationDTO authenticationDTO) throws Status440WrongPasswordException {
        JwtUser userDetails;
        userDetails = authService.loadUserByUsername(authenticationDTO.username());
        if (!passwordEncoder.matches(authenticationDTO.password(), userDetails.getPassword())) {
            throw new Status440WrongPasswordException();
        }
        return jwtUtil.generateToken(userDetails.getUsername(), (Collection<Role>) userDetails.getAuthorities());
    }
}
