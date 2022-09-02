package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
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
    public void save(long id, String text)  throws PostNotFoundException{
        Post post = postService.findPostById(id);
        if(post == null){
            throw new PostNotFoundException();
        }
        Comment comment = createComment(post, text);
        commentRepository.save(comment);
        Notification notification = new NewCommentNotification(post, comment);
        if (sponsoredPostRepository.existsByPost(post)) {
            notification.addRecipient(sponsoredPostRepository.findByPost(post).getSponsor());
        }
        notificationService.sendNotification(notification);
    }

    @Override
    public ResponseEntity<Map<String, String>> editComment(long id, String text) throws PostNotFoundException{
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new PostNotFoundException();
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())) {
            oldComment.setText(text);
            return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("Message", "That`s not your comment"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteComment(long id) throws PostNotFoundException {
        Comment oldComment = commentRepository.findCommentById(id);
        if (oldComment == null) {
            throw new PostNotFoundException();
        }
        if (oldComment.getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())
                || oldComment.getPost().getAuthor().equals(userService.findUserById(Context.getUserFromContext().getId()).getBody())) {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(Map.of("Message", "ok"),HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("Message", "That`s not your comment"),HttpStatus.BAD_REQUEST);
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
