package com.fivesysdev.Fiveogram.facadeInterfaces;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;

public interface CommentFacade {
    void addComment(long id, String text);
}
