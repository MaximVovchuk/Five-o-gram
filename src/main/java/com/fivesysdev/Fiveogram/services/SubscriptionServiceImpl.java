package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.SubscriptionNotification;
import com.fivesysdev.Fiveogram.repositories.SubscriptionRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.SubscriptionService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService, NotificationService notificationService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public User subscribe(String username, long id) throws Status431SubscriptionException, Status437UserNotFoundException {
        User newFriend = userService.findUserById(id);
        User owner = userService.findUserByUsername(username);
        if (newFriend == null) {
            throw new Status437UserNotFoundException();
        }
        if (Objects.equals(owner, newFriend)) {
            throw new Status431SubscriptionException("You can`t subscribe to yourself");
        }
        if (subscriptionRepository.findSubscriptionByFriendAndOwner(newFriend, owner) != null) {
            throw new Status431SubscriptionException("You`ve already subscribe this user");
        }
        Subscription subscription = new Subscription(owner, newFriend);
        subscriptionRepository.save(subscription);
        notificationService.sendNotification(
                new SubscriptionNotification(subscription)
        );
        return newFriend;
    }

    @Override
    public User unsubscribe(String username, long id) throws Status431SubscriptionException, Status437UserNotFoundException {
        User friend = userService.findUserById(id);
        User owner = userService.findUserByUsername(username);
        if (friend == null) {
            throw new Status437UserNotFoundException();
        }
        if (subscriptionRepository.findSubscriptionByFriendAndOwner(friend, owner) == null) {
            throw new Status431SubscriptionException("You haven`t subscribe him");
        }
        subscriptionRepository.deleteByFriendAndOwner(friend, owner);
        return friend;
    }
}
