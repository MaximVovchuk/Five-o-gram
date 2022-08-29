package com.fivesysdev.Fiveogram.serviceInterfaces;


import java.util.Map;

public interface FriendshipService {
    Map<String, String> addToFriends(long id);

    Map<String, String> unmakeFriend(long id);
}