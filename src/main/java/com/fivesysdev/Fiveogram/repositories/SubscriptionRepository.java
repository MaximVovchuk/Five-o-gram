package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByFriend_IdAndOwner_Id(Long friend_id, Long owner_id);

    boolean existsByFriendAndOwner(User friend, User owner);

    void deleteByFriendAndOwner(User friend, User owner);

    List<Subscription> findAllByOwner_Id(Long id);

    List<Subscription> findAllByFriend_id(Long id);
}
