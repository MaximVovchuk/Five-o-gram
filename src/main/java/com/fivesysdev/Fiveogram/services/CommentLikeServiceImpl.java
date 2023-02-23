package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.CommentLike;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.CommentLikeNotification;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.repositories.CommentLikeRepository;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentLikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository, NotificationService notificationService) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }


    @Override
    public Post setLike(String username, long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null) {
            throw new Status434CommentNotFoundException();
        }
        User whoLiked = userRepository.findUserByUsername(username);
        if(commentLikeRepository.existsByAuthorAndComment(whoLiked,comment)){
            return comment.getPost();
        }
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .author(whoLiked)
                .build();
        commentLikeRepository.save(commentLike);
        Notification notification = new CommentLikeNotification(commentLike);
        notificationService.sendNotification(notification);
        return comment.getPost();
    }

    @Override
    public Post deleteLike(String username, long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null) {
            throw new Status434CommentNotFoundException();
        }
        User user = userRepository.findUserByUsername(username);
        commentLikeRepository.deleteByAuthorAndComment(user, comment);
        return comment.getPost();
    }
}
