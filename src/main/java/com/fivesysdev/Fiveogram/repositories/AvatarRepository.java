package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
