package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status438PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.LikeNotification;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.repositories.LikeRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final SponsoredPostRepository sponsoredPostRepository;

    @Override
    public Post likePost(String username, Long id) throws Status438PostAlreadyLikedException, Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        User whoLikes = userService.findUserByUsername(username);
        if (likeRepository.existsByPostAndWhoLikes(post, whoLikes)) {
            throw new Status438PostAlreadyLikedException();
        }
        Like like = new Like(post, whoLikes);
        likeRepository.save(like);
        Notification notification = new LikeNotification(like);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return post;
    }

    @Override
    public Post unlikePost(String username, Long id) throws Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        likeRepository.deleteByPostAndWhoLikes(post, userService.findUserByUsername(username));
        return post;
    }

    @Override
    public Set<Like> findAllPostLikes(Long id) throws Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        return likeRepository.findAllByPost(post);
    }
}
