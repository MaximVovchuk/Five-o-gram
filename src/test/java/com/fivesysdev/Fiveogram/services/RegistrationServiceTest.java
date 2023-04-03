package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status439UsernameBusyException;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.repositories.UserToRegisterRepository;
import com.fivesysdev.Fiveogram.roles.Role;
import com.fivesysdev.Fiveogram.serviceInterfaces.MailSenderService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserToRegisterRepository userToRegisterRepository;
    @Mock
    private MailSenderService mailSenderService;
    @Mock
    private JWTUtil jwtUtil;
    private RegistrationService registrationService;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        registrationService = new RegistrationService(userRepository, passwordEncoder, userToRegisterRepository, mailSenderService, jwtUtil, new ModelMapper());
        userDTO = UserDTO.builder().username("test").password("pass").build();
        user = new User();
        user.setUsername("test");
        user.setPassword("pass");
        user.setRole(Role.USER);
    }

    @Test
    public void whenUserExists_registerUser_shouldThrowException() {
        when(userRepository.findUserByUsername("test")).thenReturn(user);
        assertThrows(Status439UsernameBusyException.class, () -> registrationService.baseRegister(userDTO));
    }

    @Test
    public void whenValidUser_thenSaveUser() throws Status439UsernameBusyException, MessagingException {
        UserDTO userDTO = UserDTO.builder().username("TestUser").password("TestPassword").email("testEmail@gmail.com").build();
        when(passwordEncoder.encode("TestPassword")).thenReturn("encodedPassword");

        String result = registrationService.baseRegister(userDTO);

        assertEquals("testEmail@gmail.com", result);
        verify(mailSenderService, times(1)).sendMessage(anyString(), anyString(), anyString());
    }

    @Test
    public void whenUserWithSameUsernameExists_thenThrowException() {
        UserDTO userDTO = UserDTO.builder().username("TestUser").password("TestPassword").build();

        when(userRepository.findUserByUsername("TestUser")).thenReturn(new User());

        assertThrows(Status439UsernameBusyException.class, () -> registrationService.baseRegister(userDTO));
    }

    @Test
    public void whenEmptyUsername_thenThrowException() {
        UserDTO userDTO = UserDTO.builder().username("").password("TestPassword").build();

        assertThrows(IllegalArgumentException.class, () -> registrationService.baseRegister(userDTO));
    }

    @Test
    public void whenEmptyPassword_thenThrowException() {
        UserDTO userDTO = UserDTO.builder().username("TestUser").password("").build();
        assertThrows(IllegalArgumentException.class, () -> registrationService.baseRegister(userDTO));
    }
}
