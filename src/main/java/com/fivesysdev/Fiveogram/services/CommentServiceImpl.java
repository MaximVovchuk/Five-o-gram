package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.notifications.NewCommentNotification;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final PostService postService;
    private final SponsoredPostRepository sponsoredPostRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, NotificationService notificationService, PostService postService, SponsoredPostRepository sponsoredPostRepository) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.postService = postService;
        this.sponsoredPostRepository = sponsoredPostRepository;
    }

    @Override
    public void save(long id, String text) {
        Post post = postService.findPostById(id);
        Comment comment = createComment(post, text);
        commentRepository.save(comment);
        Notification notification = new NewCommentNotification(post, comment);
        if(sponsoredPostRepository.existsByPost(post)){
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
    }

    @Override
    public Map<String, String> editComment(long id, String text) {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            return Map.of("Message", "Comment not found");
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()))) {
            oldComment.setText(text);
            return Map.of("Message", "ok");
        }
        return Map.of("Message", "That`s not your comment");
    }

    @Override
    public Map<String, String> deleteComment(long id) {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            return Map.of("Message", "Comment not found");
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()))
                || oldComment.getPost().getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()))) {
            commentRepository.deleteById(id);
            return Map.of("Message", "ok");
        }
        return Map.of("Message", "That`s not your comment");
    }

    public Comment createComment(Post post, String text) {
        Comment comment = new Comment();
        comment.setAuthor(Context.getUserFromContext());
        comment.setText(text);
        comment.setPublished((LocalDateTime.now()));
        comment.setPost(post);
        return comment;
    }
}
