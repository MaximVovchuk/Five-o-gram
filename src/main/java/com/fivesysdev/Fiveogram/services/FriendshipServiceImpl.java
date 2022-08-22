package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.FriendshipRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Map<String, String> addToFriends(User owner, User newFriend) {
        if (newFriend == null) {
            return Map.of("Message", "friend not found");
        }
        if (friendshipRepository.findFriendshipByFriendAndOwner(newFriend, owner) != null) {
            return Map.of("Message", "You are already friends");
        }
        Friendship friendship = new Friendship(owner, newFriend);
        try {
            friendshipRepository.save(friendship);
        } catch (Exception ex) {
            return Map.of("Message", ex.getMessage());
        }
        return Map.of("Message", "ok");
    }
}
