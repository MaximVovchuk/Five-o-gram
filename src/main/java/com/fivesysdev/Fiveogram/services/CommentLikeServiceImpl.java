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
    public Post setLike(String username, long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if(comment==null){
            throw new Status434CommentNotFoundException();
        }
        commentLikeRepository.save(
                CommentLike.builder()
                .comment(comment)
                .author(userRepository.findUserByUsername(username))
                .build()
        );
        return comment.getPost();
    }

    @Override
    @Transactional
    public Post deleteLike(String username,long id) throws Status434CommentNotFoundException {
        Comment comment = commentRepository.findCommentById(id);
        if(comment==null){
            throw new Status434CommentNotFoundException();
        }
        User user = userRepository.findUserByUsername(username);
        commentLikeRepository.deleteByAuthorAndComment(user, comment);
        return comment.getPost();
    }
}
