package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.models.notifications.TextNotification;
import com.fivesysdev.Fiveogram.repositories.NotificationRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(Notification notification){
        for(User recipient : notification.getRecipients()) {
            notificationRepository.save(new TextNotification(notification.sendNotification(), recipient));
        }
        notification.clearRecipients();
    }

    @Override
    public ResponseEntity<List<String>> getAllNotifications() {
        List<TextNotification> textNotifications = notificationRepository.findTextNotificationsByOwner(Context.getUserFromContext());
        List<String> result = new ArrayList<>();
        for(TextNotification notification : textNotifications){
            result.add(notification.getContent());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
