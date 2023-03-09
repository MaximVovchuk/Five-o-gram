package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.Subscription;
import lombok.Data;

@Data
public class SubscriptionNotification implements Notification {
    private final NotificationType type = NotificationType.SUBSCRIPTION;
    private final Subscription subscription;
    // TODO: 9/3/23 use wrappers instead of primitives
    @JsonIgnore
    private final long entityId;
    public SubscriptionNotification(Subscription subscription){
        this.subscription = subscription;
        this.entityId = subscription.getId();
        recipients.add(subscription.getFriend());
    }

    @Override
    public NotificationType getType() {
        return this.type;
    }
    @Override
    public long getEntityId() {
        return entityId;
    }
}
