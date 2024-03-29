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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    @Override
    public Subscription findSubscriptionByFriendAndOwner(User friend, User owner) {
        return subscriptionRepository.findByFriend_IdAndOwner_Id(friend.getId(), owner.getId());
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
        if (subscriptionRepository.existsByFriendAndOwner(newFriend, owner)) {
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
        if (!subscriptionRepository.existsByFriendAndOwner(friend, owner)) {
            throw new Status431SubscriptionException("You haven`t subscribe him");
        }
        subscriptionRepository.deleteByFriendAndOwner(friend, owner);
        return friend;
    }
}
