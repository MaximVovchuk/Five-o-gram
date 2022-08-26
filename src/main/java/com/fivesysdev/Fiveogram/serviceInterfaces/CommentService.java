package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;

import java.util.Map;

public interface CommentService {
    void save(Comment comment);

    Map<String ,String> editComment(long id, String text);

    Map<String,String> deleteComment(long id);
    Comment createComment(Post post, String text);
}
