package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.CommentLike;
import lombok.Data;

@Data
public class CommentLikeNotification implements Notification {
    private final NotificationType type = NotificationType.COMMENTLIKE;
    private final CommentLike commentLike;
    // TODO: 9/3/23 use wrappers instead of primitives
    @JsonIgnore
    private final long entityId;
    public CommentLikeNotification(CommentLike commentLike){
        this.commentLike = commentLike;
        this.entityId = commentLike.getId();
        recipients.add(commentLike.getComment().getAuthor());
    }

    @Override
    public NotificationType getType() {
        return this.type;
    }

    @Override
    public long getEntityId() {
        return this.entityId;
    }
}
