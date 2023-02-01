package com.fivesysdev.Fiveogram.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.models.Role;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    private RegistrationService registrationService;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        registrationService = new RegistrationService(userRepository, passwordEncoder, jwtUtil, new ModelMapper());
        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("pass");
        user = new User();
        user.setUsername("test");
        user.setPassword("pass");
        user.setRole(Role.USER);
    }

    @Test
    public void whenUserDoesNotExist_registerUser_shouldReturnToken() throws Status439UsernameBusyException {
        String expectedToken = "token";
        when(userRepository.findUserByUsername("test")).thenReturn(null);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(jwtUtil.generateToken("test", List.of(Role.USER))).thenReturn(expectedToken);
        String token = registrationService.register(userDTO);
        assertEquals(expectedToken, token);
    }

    @Test
    public void whenUserExists_registerUser_shouldThrowException() {
        when(userRepository.findUserByUsername("test")).thenReturn(user);
        assertThrows(Status439UsernameBusyException.class, () -> registrationService.register(userDTO));
    }

    @Test
    public void whenValidUser_thenSaveUser() throws Status439UsernameBusyException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("TestUser");
        userDTO.setPassword("TestPassword");
        when(passwordEncoder.encode("TestPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtUtil.generateToken("TestUser", List.of(Role.USER))).thenReturn("Token");

        String result = registrationService.register(userDTO);

        assertEquals("Token", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtUtil, times(1)).generateToken("TestUser", List.of(Role.USER));
    }

    @Test
    public void whenUserWithSameUsernameExists_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("TestUser");
        userDTO.setPassword("TestPassword");

        when(userRepository.findUserByUsername("TestUser")).thenReturn(new User());

        assertThrows(Status439UsernameBusyException.class, () -> registrationService.register(userDTO));
    }

    @Test
    public void whenEmptyUsername_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("");
        userDTO.setPassword("TestPassword");

        assertThrows(IllegalArgumentException.class, () -> registrationService.register(userDTO));
    }

    @Test
    public void whenEmptyPassword_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("TestUser");
        userDTO.setPassword("");
        assertThrows(IllegalArgumentException.class, () -> registrationService.register(userDTO));
    }
}
