package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(long id);
}
