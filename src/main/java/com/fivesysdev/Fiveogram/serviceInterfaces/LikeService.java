package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status438PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LikeService {
    ResponseEntity<Post> likePost(String username,long id) throws Status438PostAlreadyLikedException, Status435PostNotFoundException, Status437UserNotFoundException;

    ResponseEntity<Post> unlikePost(String username, long id) throws Status435PostNotFoundException;

    ResponseEntity<Set<Like>> findAllPostLikes(long id) throws Status435PostNotFoundException;
}
