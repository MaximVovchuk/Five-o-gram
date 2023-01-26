package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.SubscriptionRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    public void testSubscribe() throws Exception {
        User newFriend = User.builder().username("newFriend").build();
        newFriend.setId(1L);
        User owner = User.builder().username("testuser").build();
        owner.setId(1L);

        when(userService.findUserById(1L)).thenReturn(newFriend);
        when(userService.findUserByUsername("testuser")).thenReturn(owner);
        when(subscriptionService.findSubscriptionByFriendAndOwner(newFriend, owner)).thenReturn(null);

        User result = subscriptionService.subscribe("testuser", 1L);

        Subscription subscription = new Subscription(owner, newFriend);
        assertEquals(result, newFriend);
        verify(subscriptionRepository).findSubscriptionByFriendAndOwner(newFriend,owner);
        verify(subscriptionRepository).save(any());
        verify(notificationService).sendNotification(any());
    }

    @Test
    public void testUnsubscribe() throws Exception {
        User friend = User.builder().username("friend").build();
        friend.setId(1L);
        User owner = User.builder().username("testuser").build();
        owner.setId(1L);
        Subscription subscription = new Subscription(owner, friend);
        when(userService.findUserById(1L)).thenReturn(friend);
        when(userService.findUserByUsername("testuser")).thenReturn(owner);
        when(subscriptionRepository.findSubscriptionByFriendAndOwner(friend, owner)).thenReturn(subscription);

        User result = subscriptionService.unsubscribe("testuser", 1L);

        assertEquals(result, friend);
        verify(subscriptionRepository).deleteByFriendAndOwner(friend, owner);
    }


}