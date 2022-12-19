package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.models.notifications.*;
import com.fivesysdev.Fiveogram.repositories.*;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final LikeRepository likeRepository;
    private final CommentLikeRepository commentLikeRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   CommentRepository commentRepository,
                                   SubscriptionRepository subscriptionRepository,
                                   LikeRepository likeRepository,
                                   CommentLikeRepository commentLikeRepository) {
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.likeRepository = likeRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public void sendNotification(Notification notification) {
        for (User recipient : notification.getRecipients()) {
            notificationRepository.save(new TextNotification(recipient,
                    notification.getType(), notification.getEntityId()));
        }
        notification.clearRecipients();
    }

    @Override
    public List<Notification> getAllNotifications(String username) {
        List<TextNotification> textNotifications =
                notificationRepository.findByOwnerUsername(username);
        List<Notification> notifications = new ArrayList<>();
        for (TextNotification textNotification : textNotifications) {
            switch (textNotification.getType()) {
                case LIKE -> {
                    Optional<Like> likeOptional =
                            likeRepository.findById(textNotification.getEntityId());
                    likeOptional.ifPresent(like ->
                            notifications.add(new LikeNotification(like)));
                }
                case COMMENT -> {
                    Optional<Comment> commentOptional =
                            commentRepository.findById(textNotification.getEntityId());
                    commentOptional.ifPresent(comment ->
                            notifications.add(new CommentNotification(comment)));
                }
                case SUBSCRIPTION -> {
                    Optional<Subscription> subscriptionOptional =
                            subscriptionRepository.findById(textNotification.getEntityId());
                    subscriptionOptional.ifPresent(subscription ->
                            notifications.add(new SubscriptionNotification(subscription)));
                }
                case COMMENTLIKE -> {
                    Optional<CommentLike> commentLikeOptional =
                            commentLikeRepository.findById(textNotification.getEntityId());
                    commentLikeOptional.ifPresent(commentLike ->
                            notifications.add(new CommentLikeNotification(commentLike)));
                }
            }
        }
        return notifications;
    }

    @Scheduled(fixedRate = 86400000) //24 hours
    public void deleteOldNotifications(){
        List<TextNotification> notifications = notificationRepository.findAll();
        for (TextNotification notification : notifications) {
            if(notification.getCreatedAt().plusDays(30).isBefore(LocalDateTime.now())){
                notificationRepository.delete(notification);
            }
        }
    }
}

