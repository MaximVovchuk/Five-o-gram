package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> likePost(long id) {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        User whoLikes = userService.findUserById(Context.getUserFromContext().getId()).getBody();
        if (!likeRepository.existsByPostAndWhoLikes(post, whoLikes)) {
            likeRepository.save(new Like(post, whoLikes));
        } else {
            return new ResponseEntity<>(Map.of("Message", "You`ve already liked this post"), HttpStatus.BAD_REQUEST);
        }
        Notification notification = new NewLikeNotification(post, whoLikes);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> unlikePost(long id) {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        Like like = likeRepository.findByPostAndWhoLikes(post, Context.getUserFromContext());
        if (like == null) {
            return new ResponseEntity<>(Map.of("Message", "You didn`t like this post"), HttpStatus.BAD_REQUEST);
        }
        likeRepository.delete(like);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Set<Like>> findAllPostLikes(long id) {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return new ResponseEntity<>(likeRepository.findAllByPost(post), HttpStatus.OK);
    }
}
