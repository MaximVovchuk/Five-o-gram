package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;

public interface CommentLikeService {
    Post setLike(String username, Long id) throws Status434CommentNotFoundException;

    Post deleteLike(String username, Long id) throws Status434CommentNotFoundException;
}
