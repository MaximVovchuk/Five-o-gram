package com.fivesysdev.Fiveogram.facades;

import com.fivesysdev.Fiveogram.facadeInterfaces.FriendshipFacade;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewFriendshipNotification;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FriendshipFacadeImpl implements FriendshipFacade {
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final NotificationService notificationService;

    public FriendshipFacadeImpl(FriendshipService friendshipService, UserService userService, NotificationService notificationService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Map<String, String> addToFriends(long id) {
        User owner = Context.getUserFromContext();
        User newFriend = userService.getUser(id);
        Map<String, String> map = new HashMap<>();
        if (newFriend == null) {
            map.put("Message", "Friend not found");
        } else {
            if (newFriend.getUsername().equals(owner.getUsername())) {
                map.put("Message", "You can`t friend yourself");
            } else {
                map = friendshipService.addToFriends(owner, newFriend);
                notificationService.sendNotification(
                        new NewFriendshipNotification(owner, newFriend)
                );
            }
        }
        return map;
    }
}
