package com.fivesysdev.Fiveogram.serviceInterfaces;


import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;


public interface SubscriptionService {
    ResponseEntity<User> subscribe(String username,long id) throws Status431SubscriptionException, Status437UserNotFoundException;

    ResponseEntity<User> unsubscribe(String username,long id) throws Status431SubscriptionException, Status437UserNotFoundException;
}