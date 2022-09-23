package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.TextNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<TextNotification, Integer> {
    List<TextNotification> findTextNotificationsByOwner(User owner);
}
