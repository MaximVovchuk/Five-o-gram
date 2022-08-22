package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.User;

import java.util.Map;

public interface FriendshipService {
    Map<String, String> addToFriends(User owner, User newFriend);
}