package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    @Override
    public void addComment(Post post, Comment comment) {
        post.addComment(comment);
        commentRepository.save(comment);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void editComment(long id, String text) {
        Comment comment = createComment(id, text);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    public boolean removeComment(Comment comment) {
        return true;
    }

    public Comment createComment(long id, String text) {
        Comment comment = new Comment();
        comment.setAuthor(Context.getUserFromContext());
        comment.setText(text);
        comment.setPublished((LocalDate.now()));
        comment.setPost(postService.findPostById(id));
        return comment;
    }
}
