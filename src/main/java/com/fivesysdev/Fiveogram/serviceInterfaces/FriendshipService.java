package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.Status431FriendshipException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;


public interface FriendshipService {
    ResponseEntity<User> addToFriends(long id) throws Status431FriendshipException, Status437UserNotFoundException;

    ResponseEntity<User> unmakeFriend(long id) throws Status431FriendshipException, Status437UserNotFoundException;
}