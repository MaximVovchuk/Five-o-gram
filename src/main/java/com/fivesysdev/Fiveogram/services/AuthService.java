package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JwtUser;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public JwtUser loadUserByUsername(String credential) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(credential);
        if (user == null) {
            user = userRepository.findByEmail(credential);
            if (user == null) {
                throw new UsernameNotFoundException("User not found!");
            }
        }
        return new JwtUser(user);
    }

}
