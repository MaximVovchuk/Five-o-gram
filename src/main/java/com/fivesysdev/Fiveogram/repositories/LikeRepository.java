package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByPostAndWhoLikes(Post post, User WhoLikes);
    boolean existsByPostAndWhoLikes(Post post, User whoLikes);
}
