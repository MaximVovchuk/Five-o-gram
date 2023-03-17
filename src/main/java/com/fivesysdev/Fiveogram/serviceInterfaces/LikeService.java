package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status438PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;

import java.util.Set;

public interface LikeService {
    Post likePost(String username,Long id) throws Status438PostAlreadyLikedException, Status435PostNotFoundException, Status437UserNotFoundException;

    Post unlikePost(String username, Long id) throws Status435PostNotFoundException;

    Set<Like> findAllPostLikes(Long id) throws Status435PostNotFoundException;
}
