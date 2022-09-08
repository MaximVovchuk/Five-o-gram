package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> editComment(@PathVariable long id, @RequestParam String text) {
        try {
            return commentService.editComment(id, text);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<?> deleteComment(@PathVariable long id) {
        try {
            return commentService.deleteComment(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
