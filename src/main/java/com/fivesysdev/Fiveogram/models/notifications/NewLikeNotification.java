package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.User;

public class NewLikeNotification implements Notification{
    private final User author;
    private final User whoLikes;

    public NewLikeNotification(User author, User whoLikes) {
        this.author = author;
        this.whoLikes = whoLikes;
    }

    @Override
    public String sendNotification() {
       return this.whoLikes.getName()+" Liked "+ this.author.getName()+"`s post";
    }

    @Override
    public User getReciever() {
        return this.author;
    }
}
