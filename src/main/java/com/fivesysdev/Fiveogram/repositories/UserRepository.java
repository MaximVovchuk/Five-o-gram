package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    List<User> findByUsernameStartsWith(String startsWith);
    User findUserById(Long id);
    boolean existsByUsername(String username);
}
