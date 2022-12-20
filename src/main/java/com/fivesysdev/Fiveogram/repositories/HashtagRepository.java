package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByContent(String content);
    void deleteAllByPost(Post post);
}
