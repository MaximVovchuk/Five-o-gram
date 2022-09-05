package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;


public interface FriendshipService {
    ResponseEntity<User> addToFriends(long id);

    ResponseEntity<User> unmakeFriend(long id);
}