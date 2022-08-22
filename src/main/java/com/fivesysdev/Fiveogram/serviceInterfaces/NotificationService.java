package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.notifications.Notification;

import java.util.List;

public interface NotificationService {
    void sentNotification(Notification notification);

    List<String> getAllNotifications();
}
