package com.fivesysdev.Fiveogram.models.notifications;


import com.fivesysdev.Fiveogram.models.User;

import java.util.ArrayList;
import java.util.List;

public interface Notification {
    List<User> recipients = new ArrayList<>();

    String sendNotification();

    default void addRecipient(User user) {
        recipients.add(user);
    }

    default List<User> getRecipients() {
        return recipients;
    }

    default void clearRecipients() {
        recipients.clear();
    }
}
