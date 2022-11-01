package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.NewCommentNotification;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public Comment save(String username,long id, String text) throws Status435PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        Comment comment = createComment(username,post, text);
        commentRepository.save(comment);
        Notification notification = new NewCommentNotification(post, comment);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return comment;
    }

    @Override
    public Comment editComment(String username,long id, String text) throws Status434CommentNotFoundException, Status432NotYourCommentException {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new Status434CommentNotFoundException();
        }
        if (oldComment.getAuthor().equals(userService.findUserByUsername(username))) {
            oldComment.setText(text);
            return oldComment;
        }
        throw new Status432NotYourCommentException();
    }

    @Override
    public Post deleteComment(String username,long id) throws Status434CommentNotFoundException, Status432NotYourCommentException{
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new Status434CommentNotFoundException();
        }
        User user = userService.findUserByUsername(username);
        if (oldComment.getAuthor().equals(user)
                || oldComment.getPost().getAuthor().equals(user)){
            commentRepository.deleteById(id);
            return oldComment.getPost();
        }
        throw new Status432NotYourCommentException();
    }

    public Comment createComment(String username,Post post, String text) {
        Comment comment = new Comment();
        comment.setAuthor(userService.findUserByUsername(username));
        comment.setText(text);
        comment.setPublished((LocalDateTime.now()));
        comment.setPost(post);
        return comment;
    }
}
