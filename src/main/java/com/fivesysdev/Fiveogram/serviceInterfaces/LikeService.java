package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Like;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

public interface LikeService {
    ResponseEntity<Map<String, String>> likePost(long id);

    ResponseEntity<Map<String, String>> unlikePost(long id);
    ResponseEntity<Set<Like>> findAllPostLikes(long id);
}
