package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.FriendshipException;
import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;


public interface FriendshipService {
    ResponseEntity<User> addToFriends(long id) throws FriendshipException, UserNotFoundException;

    ResponseEntity<User> unmakeFriend(long id) throws FriendshipException, UserNotFoundException;
}