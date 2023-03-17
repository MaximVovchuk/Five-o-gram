package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;



public interface SubscriptionService {
    Subscription findSubscriptionByFriendAndOwner(User friend,User owner);
    User subscribe(String username,Long id) throws Status431SubscriptionException, Status437UserNotFoundException;

    User unsubscribe(String username,Long id) throws Status431SubscriptionException, Status437UserNotFoundException;
}