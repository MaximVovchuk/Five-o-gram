package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.User;


public class NewFriendshipNotification implements Notification {
    private final User owner;
    private final User friend;

    public NewFriendshipNotification(User owner, User friend) {
        this.owner = owner;
        this.friend = friend;
    }

    @Override
    public String sendNotification() {
        return owner.getName() + " added " + friend.getName() + " to friends";
    }

    @Override
    public User getReceiver() {
        return friend;
    }

    @Override
    public Object getObject() {
        return owner;
    }
}
