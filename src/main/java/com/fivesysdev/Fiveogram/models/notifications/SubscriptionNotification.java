package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.User;
import lombok.Data;

@Data
public class SubscriptionNotification implements Notification {
    private final NotificationType type = NotificationType.SUBSCRIPTION;
    @JsonIgnoreProperties({"password","subscriptions"})
    private final User owner;
    @JsonIgnore
    private final User friend;
    @JsonIgnore
    private final long entityId;
    @JsonIgnore
    private final long secondEntityId;


    public SubscriptionNotification(User owner, User friend) {
        this.owner = owner;
        this.friend = friend;
        this.entityId = owner.getId();
        this.secondEntityId = friend.getId();
        recipients.add(friend);
    }

    @Override
    public NotificationType getType() {
        return this.type;
    }
    @Override
    public long getEntityId() {
        return entityId;
    }
    public long getSecondEntityId() {
        return secondEntityId;
    }

    @Override
    public String sendNotification() {
        return owner.getName() + " added " + friend.getName() + " to friends";
    }

}
