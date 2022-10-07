package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

public interface CommentLikeService {
    ResponseEntity<Post> setLike(String username, long id) throws Status434CommentNotFoundException;

    ResponseEntity<Post> deleteLike(String username, long id) throws Status434CommentNotFoundException;
}