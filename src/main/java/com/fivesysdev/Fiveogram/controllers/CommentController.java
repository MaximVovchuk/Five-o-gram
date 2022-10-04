package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentLikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }

    @PatchMapping("/{id}/editComment")
    public ResponseEntity<?> editComment(@PathVariable long id, @RequestParam String text) throws Status437UserNotFoundException, Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException {
        return commentService.editComment(id, text);
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<?> deleteComment(@PathVariable long id) throws Status437UserNotFoundException, Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException {
        return commentService.deleteComment(id);
    }
    @PostMapping("/{id}/setLike")
    public ResponseEntity<Post> setLike(@PathVariable long id) throws Status434CommentNotFoundException {
        return commentLikeService.setLike(id);
    }
    @DeleteMapping("{id}/deleteLike")
    public ResponseEntity<Post> deleteLike(@PathVariable long id) throws Status434CommentNotFoundException {
        return commentLikeService.deleteLike(id);
    }
}
