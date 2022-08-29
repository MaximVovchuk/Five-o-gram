package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Like;

import java.util.Map;
import java.util.Set;

public interface LikeService {
    Map<String, String> likePost(long id);

    Map<String, String> unlikePost(long id);
    Set<Like> findAllPostLikes(long id);
}
