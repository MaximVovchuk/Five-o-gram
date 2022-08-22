package com.fivesysdev.Fiveogram.facades;

import com.fivesysdev.Fiveogram.facadeInterfaces.LikeFacade;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewLikeNotification;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import lombok.Data;
import org.springframework.stereotype.Service;

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
    public void likePost(long id) {
        Post post = postService.findPostById(id);
        likeService.likePost(post, Context.getUserFromContext());
        for (User recipient : post.getLikeNotificationRecipients()) {
            notificationService.sentNotification(
                    new NewLikeNotification(recipient, Context.getUserFromContext()));
        }
    }
}
