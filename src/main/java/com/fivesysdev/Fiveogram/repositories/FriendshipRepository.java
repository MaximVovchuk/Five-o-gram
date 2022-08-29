package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship,Integer> {
    Friendship findFriendshipByFriendAndOwner(User Friend, User owner);
    void deleteByFriendAndOwner(User friend, User owner);
}
