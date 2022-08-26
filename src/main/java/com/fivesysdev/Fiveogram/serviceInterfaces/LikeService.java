package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;

import java.util.Map;

public interface LikeService {
    void likePost(Post post, User whoLikes);

    Map<String, String> unlikePost(long id);
}
