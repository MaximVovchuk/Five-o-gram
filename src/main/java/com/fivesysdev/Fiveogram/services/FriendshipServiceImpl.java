package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewFriendshipNotification;
import com.fivesysdev.Fiveogram.repositories.FriendshipRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

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

    public ResponseEntity<Map<String, String>> addToFriends(long id) {
        User newFriend = userService.findUserById(id).getBody();
        User owner = userService.findUserById(Context.getUserFromContext().getId()).getBody();
        if (newFriend == null) {
            throw new UserNotFoundException();
        }
        if(Objects.equals(owner, newFriend)){
            return new ResponseEntity<>(Map.of("Message","You can`t friend yourself"),HttpStatus.BAD_REQUEST);
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(newFriend, owner) != null) {
            return new ResponseEntity<>(Map.of("Message", "You are already friends"),HttpStatus.BAD_REQUEST);
        }
        Friendship friendship = new Friendship(owner, newFriend);
        try {
            friendshipRepository.save(friendship);
        } catch (Exception ex) {
            return new ResponseEntity<>(Map.of("Message", ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        notificationService.sendNotification(
                new NewFriendshipNotification(owner, newFriend)
        );
        return new ResponseEntity<>(Map.of("Message", "ok"),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> unmakeFriend(long id) {
        User friend = userService.findUserById(id).getBody();
        User owner = Context.getUserFromContext();
        if (friend == null) {
            throw new UserNotFoundException();
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(friend, owner) == null) {
            return new ResponseEntity<>(Map.of("Message", "You are not friends"),HttpStatus.BAD_REQUEST);
        }
        try {
            friendshipRepository.deleteByFriendAndOwner(friend,owner);
        } catch (Exception ex) {
            return new ResponseEntity<>(Map.of("Message", ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Map.of("Message", "ok"),HttpStatus.OK);
    }
}
