package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story,Long> {
    List<Story> findAllByAuthor(User author);
}
