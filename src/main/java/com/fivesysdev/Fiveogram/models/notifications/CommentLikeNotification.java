package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.User;
import lombok.Data;

@Data
public class CommentLikeNotification implements Notification {
    private final NotificationType type = NotificationType.COMMENTLIKE;
    @JsonIgnoreProperties({"author", "published", "post", "commentLikes"})
    private final Comment comment;
    @JsonIgnoreProperties({"password", "subscriptions"})
    private final User whoLikes;
    @JsonIgnore
    private final long entityId;
    @JsonIgnore
    private final long secondEntityId;


    public CommentLikeNotification(Comment comment, User whoLikes) {
        this.comment = comment;
        this.whoLikes = whoLikes;
        this.entityId = comment.getId();
        this.secondEntityId = whoLikes.getId();
        recipients.add(comment.getAuthor());
    }

    @Override
    public NotificationType getType() {
        return this.type;
    }

    @Override
    public long getEntityId() {
        return this.entityId;
    }

    @Override
    public long getSecondEntityId() {
        return this.secondEntityId;
    }

    @Override
    public String sendNotification() {
        return this.whoLikes.getName() + " Liked " + this.comment.getAuthor().getName() + "`s comment";
    }
}
