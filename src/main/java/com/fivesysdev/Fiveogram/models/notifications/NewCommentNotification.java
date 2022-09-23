package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;

public class NewCommentNotification implements Notification {
    private final Post post;
    private final Comment comment;

    public NewCommentNotification(Post post, Comment comment) {
        this.post = post;
        this.comment = comment;
        recipients.add(post.getAuthor());
    }

    @Override
    public String sendNotification() {
        return this.comment.getAuthor().getName() + " commented "
                + this.post.getAuthor().getName() + "`s post: " +
                this.comment.getText();
    }
}