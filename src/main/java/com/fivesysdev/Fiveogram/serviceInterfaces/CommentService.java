package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;


public interface CommentService {
    Comment save(long id, String text) throws Status435PostNotFoundException;

    ResponseEntity<Comment> editComment(long id, String text) throws Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException, Status437UserNotFoundException;

    ResponseEntity<Post> deleteComment(long id) throws Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException, Status437UserNotFoundException;

    Comment createComment(Post post, String text);
}
