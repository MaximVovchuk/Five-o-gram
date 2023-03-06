package com.fivesysdev.Fiveogram.models.notifications;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface Notification {
    @JsonIgnore
    List<User> recipients = new ArrayList<>();

    NotificationType type = null;
    long entityId = 0;
    LocalDateTime createdAt = null;

    default void addRecipient(User user) {
        recipients.add(user);
    }

    @JsonIgnore
    default List<User> getRecipients() {
        return recipients;
    }

    default void setRecipients(List<User> users) {
        recipients.clear();
        recipients.addAll(users);
    }
    default void clearRecipients() {
        recipients.clear();
    }

    default long getEntityId() {
        return entityId;
    }

    default NotificationType getType() {
        return type;
    }
}
