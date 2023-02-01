package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        User testuser = User.builder().username("testuser").build();
        testuser.setId(1L);
        User testfriend = User.builder().username("testfriend").build();
        testfriend.setId(2L);

        when(userService.findUserById(2L)).thenReturn(testfriend);
        when(userService.findUserByUsername("testuser")).thenReturn(testuser);
        when(subscriptionRepository.existsByFriendAndOwner(testfriend, testuser)).thenReturn(false);

        User result = subscriptionService.subscribe("testuser", 2L);

        assertEquals(result, testfriend);
        verify(subscriptionRepository).existsByFriendAndOwner(testfriend, testuser);
        verify(subscriptionRepository).save(any());
        verify(notificationService).sendNotification(any());
    }

    @Test
    public void testSubscribeFriendIsNull() throws Status437UserNotFoundException {
        when(userService.findUserById(1L)).thenReturn(null);

        assertThrows(Status437UserNotFoundException.class,
                () -> subscriptionService.subscribe("testuser", 1L));
    }

    @Test
    public void testSubscribeYourself() throws Status437UserNotFoundException {
        User testuser = User.builder().username("testuser").build();
        testuser.setId(1L);

        when(userService.findUserById(1L)).thenReturn(testuser);
        when(userService.findUserByUsername("testuser")).thenReturn(testuser);

        assertThrows(Status431SubscriptionException.class,
                () -> subscriptionService.subscribe("testuser", 1L));
    }

    @Test
    public void testSubscribeIfYouAlreadySubscribedHim() throws Status437UserNotFoundException {
        User testuser = User.builder().username("testuser").build();
        testuser.setId(1L);
        User testfriend = User.builder().username("testfriend").build();
        testfriend.setId(2L);

        when(userService.findUserById(2L)).thenReturn(testfriend);
        when(userService.findUserByUsername("testuser")).thenReturn(testuser);
        when(subscriptionRepository.existsByFriendAndOwner(testfriend,testuser)).thenReturn(true);

        assertThrows(Status431SubscriptionException.class,
                () -> subscriptionService.subscribe("testuser", 2L));
    }

    @Test
    public void testUnsubscribe() throws Exception {
        User friend = User.builder().username("friend").build();
        friend.setId(1L);
        User owner = User.builder().username("testuser").build();
        owner.setId(1L);
        when(userService.findUserById(1L)).thenReturn(friend);
        when(userService.findUserByUsername("testuser")).thenReturn(owner);
        when(subscriptionRepository.existsByFriendAndOwner(friend, owner)).thenReturn(true);

        User result = subscriptionService.unsubscribe("testuser", 1L);

        assertEquals(result, friend);
        verify(subscriptionRepository).deleteByFriendAndOwner(friend, owner);
    }

    @Test
    public void testUnsubscribeFriendIsNull() throws Status437UserNotFoundException {
        when(userService.findUserById(1L)).thenReturn(null);

        assertThrows(Status437UserNotFoundException.class,
                () -> subscriptionService.unsubscribe("testuser", 1L));
    }

    @Test
    public void testUnsubscribeIfYouDidNotSubscribeHim() throws Status437UserNotFoundException {
        String username = "testuser";
        long userId = 1L;
        String friendname = "testfriend";
        long friendId = 2L;

        User testuser = User.builder().username(username).build();
        testuser.setId(userId);
        User testfriend = User.builder().username(friendname).build();
        testfriend.setId(friendId);

        when(userService.findUserById(friendId)).thenReturn(testfriend);
        when(userService.findUserByUsername(username)).thenReturn(testuser);
        when(subscriptionRepository.existsByFriendAndOwner(testfriend,testuser)).thenReturn(false);

        assertThrows(Status431SubscriptionException.class,
                () -> subscriptionService.unsubscribe(username, friendId));
    }
}