package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.Status402FriendshipException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;


public interface FriendshipService {
    ResponseEntity<User> addToFriends(long id) throws Status402FriendshipException, Status404UserNotFoundException;

    ResponseEntity<User> unmakeFriend(long id) throws Status402FriendshipException, Status404UserNotFoundException;
}