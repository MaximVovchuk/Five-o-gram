package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.Like;
import lombok.Data;

@Data
public class LikeNotification implements Notification {
    private final NotificationType type = NotificationType.LIKE;
    //just Like
    private final Like like;
    @JsonIgnore
    private final long entityId;

    public LikeNotification(Like like){
        this.like = like;
        this.entityId = like.getId();
        recipients.add(like.getPost().getAuthor());
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
