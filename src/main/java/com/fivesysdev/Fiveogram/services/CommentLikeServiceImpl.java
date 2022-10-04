package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.CommentLike;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.CommentLikeRepository;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentLikeService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public ResponseEntity<Post> setLike(long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if(comment==null){
            throw new Status434CommentNotFoundException();
        }
        commentLikeRepository.save(
                CommentLike.builder()
                .comment(comment)
                .author(userRepository.findUserById(Context.getUserFromContext().getId()))
                .build()
        );
        return new ResponseEntity<>(comment.getPost(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Post> deleteLike(long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if(comment==null){
            throw new Status434CommentNotFoundException();
        }
        User user = userRepository.findUserById(Context.getUserFromContext().getId());
        commentLikeRepository.deleteByAuthorAndComment(user, comment);
        return new ResponseEntity<>(comment.getPost(), HttpStatus.OK);
    }
}
