package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewFriendshipNotification;
import com.fivesysdev.Fiveogram.repositories.FriendshipRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepository, UserService userService, NotificationService notificationService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Map<String, String> addToFriends(long id) {
        User newFriend = userService.findUserById(id);
        User owner = userService.findUserById(Context.getUserFromContext().getId());
        if (newFriend == null) {
            return Map.of("Message", "friend not found");
        }
        if(owner.equals(newFriend)){
            return Map.of("Message","You can`t friend yourself");
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(newFriend, owner) != null) {
            return Map.of("Message", "You are already friends");
        }
        Friendship friendship = new Friendship(owner, newFriend);
        try {
            friendshipRepository.save(friendship);
        } catch (Exception ex) {
            return Map.of("Message", ex.getMessage());
        }
        notificationService.sendNotification(
                new NewFriendshipNotification(owner, newFriend)
        );
        return Map.of("Message", "ok");
    }

    @Override
    public Map<String, String> unmakeFriend(long id) {
        User friend = userService.findUserById(id);
        User owner = Context.getUserFromContext();
        if (friend == null) {
            return Map.of("Message", "friend not found");
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(friend, owner) == null) {
            return Map.of("Message", "You are not friends");
        }
        try {
            friendshipRepository.deleteByFriendAndOwner(friend,owner);
        } catch (Exception ex) {
            return Map.of("Message", ex.getMessage());
        }
        return Map.of("Message", "ok");
    }
}
