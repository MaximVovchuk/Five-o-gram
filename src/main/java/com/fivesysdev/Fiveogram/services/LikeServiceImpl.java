package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status438PostAlreadyLikedException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Post likePost(String username,long id) throws Status438PostAlreadyLikedException, Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        User whoLikes = userService.findUserByUsername(username);
        if (likeRepository.existsByPostAndWhoLikes(post, whoLikes)) {
            throw new Status438PostAlreadyLikedException();
        }
        likeRepository.save(new Like(post, whoLikes));
        Notification notification = new NewLikeNotification(post, whoLikes);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return post;
    }

    @Override
    public Post unlikePost(String username,long id) throws Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        likeRepository.deleteByPostAndWhoLikes(post, userService.findUserByUsername(username));
        return post;
    }

    @Override
    public Set<Like> findAllPostLikes(long id) throws Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        return likeRepository.findAllByPost(post);
    }
}
