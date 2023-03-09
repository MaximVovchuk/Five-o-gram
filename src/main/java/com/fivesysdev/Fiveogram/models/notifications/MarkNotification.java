package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import lombok.Data;

@Data
public class MarkNotification implements Notification{
    private final NotificationType type = NotificationType.MARK;
    private final Post post;
    // TODO: 9/3/23 use wrappers instead of primitives
    @JsonIgnore
    private final long entityId;

    public MarkNotification(Post post, User user){
        this.post = post;
        this.entityId = post.getId();
        recipients.add(user);
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


