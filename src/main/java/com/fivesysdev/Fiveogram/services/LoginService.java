package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.config.JwtUser;
import com.fivesysdev.Fiveogram.dto.AuthenticationDTO;
import com.fivesysdev.Fiveogram.exceptions.Status440WrongPasswordException;
import com.fivesysdev.Fiveogram.models.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LoginService {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public LoginService(AuthService authService, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(AuthenticationDTO authenticationDTO) throws Status440WrongPasswordException {
        JwtUser userDetails;
        userDetails = authService.loadUserByUsername(authenticationDTO.getUsername());
        if (!passwordEncoder.matches(authenticationDTO.getPassword(), userDetails.getPassword())) {
            throw new Status440WrongPasswordException();
        }
        return jwtUtil.generateToken(userDetails.getUsername(), (Collection<Role>) userDetails.getAuthorities());
    }
}
