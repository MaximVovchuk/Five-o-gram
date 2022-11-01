package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.notifications.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);

    List<String> getAllNotifications(String username);
}
