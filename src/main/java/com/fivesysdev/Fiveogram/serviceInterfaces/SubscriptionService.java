package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;

import java.util.List;


public interface SubscriptionService {
    User subscribe(String username,long id) throws Status431SubscriptionException, Status437UserNotFoundException;

    User unsubscribe(String username,long id) throws Status431SubscriptionException, Status437UserNotFoundException;
}