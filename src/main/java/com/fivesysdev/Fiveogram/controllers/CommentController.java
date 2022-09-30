package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/{id}/editComment")
    public ResponseEntity<?> editComment(@PathVariable long id, @RequestParam String text) throws Status437UserNotFoundException, Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException {
        return commentService.editComment(id, text);
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<?> deleteComment(@PathVariable long id) throws Status437UserNotFoundException, Status435PostNotFoundException, Status434CommentNotFoundException, Status432NotYourCommentException {
        return commentService.deleteComment(id);
    }
}
