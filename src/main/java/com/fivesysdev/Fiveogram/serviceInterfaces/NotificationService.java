package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.notifications.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);

    List<Notification> getAllNotifications(String username) throws Status435PostNotFoundException, Status437UserNotFoundException;
}
