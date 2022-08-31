package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CommentService {
    void save(long id, String text) throws PostNotFoundException;

    ResponseEntity<Map<String, String>> editComment(long id, String text) throws PostNotFoundException;

    ResponseEntity<Map<String, String>> deleteComment(long id) throws PostNotFoundException;
    Comment createComment(Post post, String text);
}
