package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.models.Comment;
import lombok.Data;

@Data
public class CommentNotification implements Notification {
    private final NotificationType type = NotificationType.COMMENT;
    private final Comment comment;
    @JsonIgnore
    private final Long entityId;

    @Override
    public NotificationType getType() {
        return this.type;
    }
    @Override
    public Long getEntityId() {
        return entityId;
    }
    public CommentNotification(Comment comment) {
        this.comment = comment;
        this.entityId = comment.getId();
        recipients.add(comment.getPost().getAuthor());
    }
}