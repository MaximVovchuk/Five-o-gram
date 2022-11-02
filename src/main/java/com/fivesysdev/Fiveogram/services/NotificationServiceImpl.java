package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.*;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.NotificationRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   PostService postService,
                                   UserService userService, CommentRepository commentRepository) {
        this.notificationRepository = notificationRepository;
        this.postService = postService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    public void sendNotification(Notification notification) {
        for (User recipient : notification.getRecipients()) {
            notificationRepository.save(new TextNotification(notification.sendNotification(),
                    recipient, notification.getType(), notification.getEntityId(), notification.getSecondEntityId()));
        }
        notification.clearRecipients();
    }

    @Override
    public List<Notification> getAllNotifications(String username) throws Status435PostNotFoundException, Status437UserNotFoundException {
        List<TextNotification> textNotifications =
                notificationRepository.findByOwnerUsername(username);
        List<Notification> notifications = new ArrayList<>();
        for (TextNotification textNotification : textNotifications) {
            switch (textNotification.getType()) {
                case LIKE -> notifications.add(new LikeNotification(
                        postService.findPostById(textNotification.getEntityId()),
                        userService.findUserById(textNotification.getSecondEntityId())
                ));
                case COMMENT -> notifications.add(new CommentNotification(
                        postService.findPostById(textNotification.getEntityId()),
                        commentRepository.findCommentById(textNotification.getSecondEntityId())
                ));
                case SUBSCRIPTION -> notifications.add(new SubscriptionNotification(
                        userService.findUserById(textNotification.getEntityId()),
                        userService.findUserById(textNotification.getSecondEntityId())
                ));
                case COMMENTLIKE -> notifications.add(new CommentLikeNotification(
                        commentRepository.findCommentById(textNotification.getEntityId()),
                        userService.findUserById(textNotification.getSecondEntityId())
                ));
            }
        }
        return notifications;
    }
}

