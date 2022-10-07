package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findSubscriptionByFriendAndOwner(User Friend, User owner);

    void deleteByFriendAndOwner(User friend, User owner);
}
