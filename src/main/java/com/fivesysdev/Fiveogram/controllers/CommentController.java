package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
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
    private final JWTUtil jwtUtil;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
        this.jwtUtil = jwtUtil;
    }

    @PatchMapping("/{id}/editComment")
    public ResponseEntity<Comment> editComment(@PathVariable long id, @RequestParam String text,
                                               @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status435PostNotFoundException,
            Status434CommentNotFoundException, Status432NotYourCommentException {
        return ResponseEntity.ok(commentService.editComment(jwtUtil.getUsername(token), id, text));
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<Post> deleteComment(@PathVariable long id,
                                              @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status435PostNotFoundException,
            Status434CommentNotFoundException, Status432NotYourCommentException {
        return ResponseEntity.ok(commentService.deleteComment(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id}/setLike")
    public ResponseEntity<Post> setLike(@PathVariable long id,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status434CommentNotFoundException {
        return ResponseEntity.ok(commentLikeService.setLike(jwtUtil.getUsername(token), id));
    }

    @DeleteMapping("{id}/deleteLike")
    public ResponseEntity<Post> deleteLike(@PathVariable long id,
                                           @RequestHeader(value = "Authorization") String token)
            throws Status434CommentNotFoundException {
        return ResponseEntity.ok(commentLikeService.deleteLike(jwtUtil.getUsername(token), id));
    }
}
