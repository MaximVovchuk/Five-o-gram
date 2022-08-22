package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;

public interface LikeService {
    void likePost(Post post, User whoLikes);

     void unlikePost(long id);
}
