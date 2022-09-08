package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LikeService {
    ResponseEntity<Post> likePost(long id) throws PostAlreadyLikedException, PostNotFoundException, UserNotFoundException;

    ResponseEntity<Post> unlikePost(long id) throws PostNotFoundException;
    ResponseEntity<Set<Like>> findAllPostLikes(long id) throws PostNotFoundException;
}
