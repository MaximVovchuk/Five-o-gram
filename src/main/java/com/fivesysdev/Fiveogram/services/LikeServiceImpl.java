package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewLikeNotification;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.repositories.LikeRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final SponsoredPostRepository sponsoredPostRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostService postService, UserService userService, NotificationService notificationService, SponsoredPostRepository sponsoredPostRepository) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.sponsoredPostRepository = sponsoredPostRepository;
    }

    @Override
    public Map<String, String> likePost(long id) {
        Post post = postService.findPostById(id);
        if(post == null){
            return Map.of("Message", "Post isn`t found");
        }
        User whoLikes = userService.findUserById(Context.getUserFromContext().getId());
        if (!likeRepository.existsByPostAndWhoLikes(post, whoLikes)) {
            likeRepository.save(new Like(post, whoLikes));
        } else {
            return Map.of("Message","You`ve already liked this post");
        }
        Notification notification = new NewLikeNotification(post,whoLikes);
        if(sponsoredPostRepository.existsByPost(post)){
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return Map.of("Message","ok");
    }

    @Override
    public Map<String, String> unlikePost(long id) {
        Post post = postService.findPostById(id);
        if (post == null) {
            return Map.of("Message", "Post not found");
        }
        Like like = likeRepository.findByPostAndWhoLikes(post, Context.getUserFromContext());
        if (like == null) {
            return Map.of("Message", "You didn`t like this post");
        }
        likeRepository.delete(like);
        return Map.of("Message", "ok");
    }

    @Override
    public Set<Like> findAllPostLikes(long id) {
        Post post = postService.findPostById(id);
        return likeRepository.findAllByPost(post);
    }
}
