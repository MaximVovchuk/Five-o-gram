package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;


public class NewCommentNotification implements Notification {
    private final Post post;
    private final Comment comment;

    public NewCommentNotification(Post post, Comment comment) {
        this.post = post;
        this.comment = comment;
    }

    @Override
    public String sendNotification() {
         return this.comment.getAuthor().getName() + " commented "
                + this.post.getAuthor().getName() + "`s post: " +
                this.comment.getText();
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