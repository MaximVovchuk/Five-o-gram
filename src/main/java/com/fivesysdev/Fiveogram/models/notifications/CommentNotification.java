package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.Data;

@Data
public class CommentNotification implements Notification {
    private final NotificationType type = NotificationType.COMMENT;
    @JsonIgnoreProperties({"author","pubDate","likesList","commentList"})
    private final Post post;
    @JsonIgnoreProperties({"published","post","commentLikes"})
    private final Comment comment;
    @JsonIgnore
    private final long entityId;
    @JsonIgnore
    private final long secondEntityId;

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
    public CommentNotification(Post post, Comment comment) {
        this.post = post;
        this.comment = comment;
        this.entityId = post.getId();
        this.secondEntityId = comment.getId();
        recipients.add(post.getAuthor());
    }

    @Override
    public String sendNotification() {
        return this.comment.getAuthor().getName() + " commented "
                + this.post.getAuthor().getName() + "`s post: " +
                this.comment.getText();
    }
}