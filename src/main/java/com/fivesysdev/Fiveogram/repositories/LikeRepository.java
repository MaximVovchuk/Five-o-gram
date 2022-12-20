package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPostAndWhoLikes(Post post, User whoLikes);

    Set<Like> findAllByPost(Post post);
    List<Like> findAllByWhoLikes(User whoLikes);

    void deleteByPostAndWhoLikes(Post post, User whoLikes);
}
