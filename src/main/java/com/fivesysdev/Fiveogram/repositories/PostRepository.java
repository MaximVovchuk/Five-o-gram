package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByAuthor(User author);

    Post findPostById(long id);

    void deleteById(long id);
}
