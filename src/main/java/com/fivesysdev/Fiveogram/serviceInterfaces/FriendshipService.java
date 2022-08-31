package com.fivesysdev.Fiveogram.serviceInterfaces;


import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface FriendshipService {
    ResponseEntity<Map<String, String>> addToFriends(long id);

    ResponseEntity<Map<String, String>> unmakeFriend(long id);
}