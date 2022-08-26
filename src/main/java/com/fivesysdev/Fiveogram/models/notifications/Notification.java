package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.User;


public interface Notification {
    String sendNotification();
    User getReceiver();
    Object getObject();
}
