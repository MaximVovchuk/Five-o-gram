package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;


public class NewLikeNotification implements Notification{
    private final Post post;
    private final User whoLikes;

    public NewLikeNotification(Post post, User whoLikes) {
        this.post = post;
        this.whoLikes = whoLikes;
    }

    @Override
    public String sendNotification() {
       return this.whoLikes.getName()+" Liked "+ this.post.getAuthor().getName()+"`s post";
    }

    @Override
    public User getReceiver() {
        return post.getAuthor();
    }

    @Override
    public Object getObject() {
        return post;
    }
}
