package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;


public interface CommentService {
    Comment save(long id, String text) throws PostNotFoundException;

    ResponseEntity<Comment> editComment(long id, String text) throws PostNotFoundException, CommentNotFoundException, NotYourCommentException, UserNotFoundException;

    ResponseEntity<Post> deleteComment(long id) throws PostNotFoundException, CommentNotFoundException, NotYourCommentException, UserNotFoundException;
    Comment createComment(Post post, String text);
}
