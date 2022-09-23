package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status402FriendshipException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
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

    public ResponseEntity<User> addToFriends(long id) throws Status402FriendshipException, Status404UserNotFoundException {
        User newFriend = userService.findUserById(id).getBody();
        User owner = userService.findUserById(Context.getUserFromContext().getId()).getBody();
        if (newFriend == null) {
            throw new Status404UserNotFoundException();
        }
        if (Objects.equals(owner, newFriend)) {
            throw new Status402FriendshipException("You can`t friend yourself");
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(newFriend, owner) != null) {
            throw new Status402FriendshipException("You are already friends");
        }
        Friendship friendship = new Friendship(owner, newFriend);
        friendshipRepository.save(friendship);
        notificationService.sendNotification(
                new NewFriendshipNotification(owner, newFriend)
        );
        return new ResponseEntity<>(newFriend, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> unmakeFriend(long id) throws Status402FriendshipException, Status404UserNotFoundException {
        User friend = userService.findUserById(id).getBody();
        User owner = Context.getUserFromContext();
        if (friend == null) {
            throw new Status404UserNotFoundException();
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(friend, owner) == null) {
            throw new Status402FriendshipException("You are not friends");
        }
        friendshipRepository.deleteByFriendAndOwner(friend, owner);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }
}
