package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status403NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status404CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Comment save(long id, String text) throws Status404PostNotFoundException {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new Status404PostNotFoundException();
        }
        Comment comment = createComment(post, text);
        commentRepository.save(comment);
        Notification notification = new NewCommentNotification(post, comment);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
        return comment;
    }

    @Override
    public ResponseEntity<Comment> editComment(long id, String text) throws Status404CommentNotFoundException, Status403NotYourCommentException, Status404UserNotFoundException {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new Status404CommentNotFoundException();
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())) {
            oldComment.setText(text);
            return new ResponseEntity<>(oldComment, HttpStatus.OK);
        }
        throw new Status403NotYourCommentException();
    }

    @Override
    public ResponseEntity<Post> deleteComment(long id) throws Status404CommentNotFoundException, Status403NotYourCommentException, Status404UserNotFoundException {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new Status404CommentNotFoundException();
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())
                || oldComment.getPost().getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())) {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(oldComment.getPost(), HttpStatus.OK);
        }
        throw new Status403NotYourCommentException();
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
