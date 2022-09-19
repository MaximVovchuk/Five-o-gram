package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status405PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LikeService {
    ResponseEntity<Post> likePost(long id) throws Status405PostAlreadyLikedException, Status404PostNotFoundException, Status404UserNotFoundException;

    ResponseEntity<Post> unlikePost(long id) throws Status404PostNotFoundException;
    ResponseEntity<Set<Like>> findAllPostLikes(long id) throws Status404PostNotFoundException;
}
