package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.Status403NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status404CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404UserNotFoundException;
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
    public ResponseEntity<?> editComment(@PathVariable long id, @RequestParam String text) throws Status404UserNotFoundException, Status404PostNotFoundException, Status404CommentNotFoundException, Status403NotYourCommentException {
        return commentService.editComment(id, text);
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<?> deleteComment(@PathVariable long id) throws Status404UserNotFoundException, Status404PostNotFoundException, Status404CommentNotFoundException, Status403NotYourCommentException {
        return commentService.deleteComment(id);
    }
}
