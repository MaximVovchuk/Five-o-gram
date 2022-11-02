package com.fivesysdev.Fiveogram.models.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import lombok.Data;

@Data
public class LikeNotification implements Notification {
    private final NotificationType type = NotificationType.LIKE;
    @JsonIgnoreProperties({"author","pubDate","likesList","commentList"})
    private final Post post;
    @JsonIgnoreProperties({"password","subscriptions"})
    private final User whoLikes;
    @JsonIgnore
    private final long entityId;
    @JsonIgnore
    private final long secondEntityId;


    public LikeNotification(Post post, User whoLikes) {
        this.post = post;
        this.whoLikes = whoLikes;
        this.entityId = post.getId();
        this.secondEntityId = whoLikes.getId();
        recipients.add(post.getAuthor());
    }
    @Override
    public NotificationType getType() {
        return this.type;
    }
    @Override
    public long getEntityId() {
        return entityId;
    }
    @Override
    public long getSecondEntityId() {
        return secondEntityId;
    }

    @Override
    public String sendNotification() {
        return this.whoLikes.getName() + " Liked " + this.post.getAuthor().getName() + "`s post";
    }
}
