package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;

public interface CommentService {
    void addComment(Post post, Comment comment);
    void save(long id, String text);

    void editComment(long id, String text);

    void deleteComment(long id);
    Comment createComment(long id, String text);
}
