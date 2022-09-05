package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LikeService {
    ResponseEntity<Post> likePost(long id);

    ResponseEntity<Post> unlikePost(long id);
    ResponseEntity<Set<Like>> findAllPostLikes(long id);
}
