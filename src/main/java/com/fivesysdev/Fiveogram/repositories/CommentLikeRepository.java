package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.CommentLike;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    void deleteByAuthorAndComment(User author, Comment comment);
}
