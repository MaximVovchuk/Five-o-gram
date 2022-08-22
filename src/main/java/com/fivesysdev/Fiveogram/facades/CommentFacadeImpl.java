package com.fivesysdev.Fiveogram.facades;

import com.fivesysdev.Fiveogram.facadeInterfaces.CommentFacade;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.notifications.NewCommentNotification;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import org.springframework.stereotype.Service;

@Service
public class CommentFacadeImpl implements CommentFacade {
    private final CommentService commentService;
    private final PostService postService;
    private final NotificationService notificationService;

    public CommentFacadeImpl(CommentService commentService, PostService postService, NotificationService notificationService) {
        this.commentService = commentService;
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @Override
    public void addComment(long id, String text) {
        Post post = postService.findPostById(id);
        Comment comment = commentService.createComment(id,text);
        commentService.save(id , text);
        notificationService.sentNotification(
                new NewCommentNotification(post, comment)
        );
    }
}
