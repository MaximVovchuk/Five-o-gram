package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status404CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status403NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;


public interface CommentService {
    Comment save(long id, String text) throws Status404PostNotFoundException;

    ResponseEntity<Comment> editComment(long id, String text) throws Status404PostNotFoundException, Status404CommentNotFoundException, Status403NotYourCommentException, Status404UserNotFoundException;

    ResponseEntity<Post> deleteComment(long id) throws Status404PostNotFoundException, Status404CommentNotFoundException, Status403NotYourCommentException, Status404UserNotFoundException;
    Comment createComment(Post post, String text);
}
