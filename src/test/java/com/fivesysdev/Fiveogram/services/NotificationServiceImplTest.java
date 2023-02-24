package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.models.notifications.*;
import com.fivesysdev.Fiveogram.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private NotificationServiceImpl notificationService;

//    @Test
//    void sendNotification_validInput_savesNotification() {
//        Subscription subscription = new Subscription(new User(), new User());
//        Notification notification = new SubscriptionNotification(subscription);
//        notificationService.sendNotification(notification);
//
//        verify(notificationRepository).save(any(TextNotification.class));
//    }

    @Test
    public void testGetAllNotifications() {
        String username = "test_user";
        List<TextNotification> textNotifications = new ArrayList<>();
        textNotifications.add(new TextNotification(new User(), NotificationType.LIKE, 1L));
        textNotifications.add(new TextNotification(new User(), NotificationType.COMMENT, 2L));
        textNotifications.add(new TextNotification(new User(), NotificationType.SUBSCRIPTION, 3L));
        textNotifications.add(new TextNotification(new User(), NotificationType.COMMENTLIKE, 4L));
        textNotifications.add(new TextNotification(new User(), NotificationType.MARK, 5L));

        when(notificationRepository.findByOwnerUsername(username)).thenReturn(textNotifications);

        Post post = new Post();
        post.setAuthor(new User());
        post.setId(1L);
        Like like = new Like(post, new User());
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(new User());
        Subscription subscription = new Subscription(new User(), new User());
        CommentLike commentLike = new CommentLike(new User(), comment);
        User user = new User();

        when(likeRepository.findById(1L)).thenReturn(Optional.of(like));
        when(commentRepository.findById(2L)).thenReturn(Optional.of(comment));
        when(subscriptionRepository.findById(3L)).thenReturn(Optional.of(subscription));
        when(commentLikeRepository.findById(4L)).thenReturn(Optional.of(commentLike));
        when(postRepository.findById(5L)).thenReturn(Optional.of(post));
        when(userRepository.findUserByUsername(username)).thenReturn(user);

        List<Notification> notifications = notificationService.getAllNotifications(username);

        assertEquals(5, notifications.size());
        assertTrue(notifications.get(0) instanceof LikeNotification);
        assertTrue(notifications.get(1) instanceof CommentNotification);
        assertTrue(notifications.get(2) instanceof SubscriptionNotification);
        assertTrue(notifications.get(3) instanceof CommentLikeNotification);
        assertTrue(notifications.get(4) instanceof MarkNotification);

    }

    @Test
    public void testDeleteOldNotifications() {
        List<TextNotification> notifications = new ArrayList<>();
        User user = new User();
        TextNotification oldNotification = new TextNotification(user, NotificationType.COMMENT, 1);
        oldNotification.setCreatedAt(LocalDateTime.now().minusDays(40));
        notifications.add(oldNotification);
        TextNotification newNotification = new TextNotification(user, NotificationType.COMMENT, 2);
        newNotification.setCreatedAt(LocalDateTime.now().minusDays(10));
        notifications.add(newNotification);
        when(notificationRepository.findAll()).thenReturn(notifications);

        notificationService.deleteOldNotifications();

        verify(notificationRepository, times(1)).delete(oldNotification);
        verify(notificationRepository, times(0)).delete(newNotification);
    }
}