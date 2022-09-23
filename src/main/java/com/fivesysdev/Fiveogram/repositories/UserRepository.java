package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

    User findUserById(long id);
}
