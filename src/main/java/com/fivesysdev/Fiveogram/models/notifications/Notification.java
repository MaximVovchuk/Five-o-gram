package com.fivesysdev.Fiveogram.models.notifications;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.User;

import java.util.ArrayList;
import java.util.List;

public interface Notification {
    @JsonIgnore
    List<User> recipients = new ArrayList<>();

    NotificationType type = null;
    long entityId = 0;
    long secondEntityId = 0;


    String sendNotification();

    default void addRecipient(User user) {
        recipients.add(user);
    }
    @JsonIgnore
    default List<User> getRecipients() {
        return recipients;
    }

    default void clearRecipients() {
        recipients.clear();
    }

    default long getEntityId() {
        return entityId;
    }
    default long getSecondEntityId(){
        return secondEntityId;
    }
    default NotificationType getType() {
        return type;
    }
}
