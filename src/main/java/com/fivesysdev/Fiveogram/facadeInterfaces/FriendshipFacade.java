package com.fivesysdev.Fiveogram.facadeInterfaces;

import com.fivesysdev.Fiveogram.models.User;

import java.util.Map;

public interface FriendshipFacade {
    Map<String, String> addToFriends(long id);
}
