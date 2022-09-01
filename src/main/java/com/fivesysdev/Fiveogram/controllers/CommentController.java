package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/{id}/editComment")
    public ResponseEntity<Map<String, String>> editComment(@PathVariable long id, @RequestParam String text) {
        try {
            return commentService.editComment(id, text);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @DeleteMapping("/{id}/deleteComment")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable long id) {
        try {
            return commentService.deleteComment(id);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }
}
