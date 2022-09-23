package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.SponsoredPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsoredPostRepository extends JpaRepository<SponsoredPost, Long> {
    SponsoredPost findByPost(Post post);

    boolean existsByPost(Post post);

    void deleteByPost(Post post);
}
