package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;


public interface CommentService {
    Comment save(String username,long id, String text) throws Status435PostNotFoundException, Status448TextIsNullException;

    Comment editComment(String username,long id, String text) throws Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException, Status437UserNotFoundException;

    Post deleteComment(String username,long id) throws Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException, Status437UserNotFoundException;

    Comment createComment(String username,Post post, String text);
}
