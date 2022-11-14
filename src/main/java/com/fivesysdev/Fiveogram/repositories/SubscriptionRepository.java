package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findSubscriptionByFriendAndOwner(User Friend, User owner);
    void deleteByFriendAndOwner(User friend, User owner);
    List<Subscription> findAllByOwner_Id(long id);
    List<Subscription> findAllByFriend_id(long id);
}
