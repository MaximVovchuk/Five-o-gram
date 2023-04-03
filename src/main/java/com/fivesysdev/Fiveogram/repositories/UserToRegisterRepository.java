package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.UserToRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToRegisterRepository extends JpaRepository<UserToRegister, Long> {
    UserToRegister findByEmail(String email);
}
