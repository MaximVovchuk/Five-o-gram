package com.fivesysdev.Fiveogram.facades;

import com.fivesysdev.Fiveogram.facadeInterfaces.LikeFacade;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewLikeNotification;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LikeFacadeImpl implements LikeFacade {
    private final LikeService likeService;
    private final PostService postService;
    private final NotificationService notificationService;

    public LikeFacadeImpl(LikeService likeService, PostService postService, NotificationService notificationService) {
        this.likeService = likeService;
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @Override
    public Map<String,String> likePost(long id) {
        Post post = postService.findPostById(id);
        if(post==null){
            return Map.of("Message","post not found");
        }
        likeService.likePost(post, Context.getUserFromContext());
        for (User recipient : post.getLikeNotificationRecipients()) {
            notificationService.sentNotification(
                    new NewLikeNotification(recipient, Context.getUserFromContext()));
        }
        return Map.of("Message","ok");
    }
}
