package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Mark;
import com.fivesysdev.Fiveogram.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark,Long> {
    List<Mark> findByUsername(String username);
    void deleteByPicture(Picture picture);
    List<Mark> findAllByPicture(Picture picture);
}
