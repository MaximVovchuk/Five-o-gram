package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.notifications.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);

    ResponseEntity<List<String>> getAllNotifications(String username);
}
