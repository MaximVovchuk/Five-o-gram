package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/editComment")
    public Map<String, String> editComment(@PathVariable long id, @RequestBody String text) {
        return commentService.editComment(id, text);
    }

    @PostMapping("/{id}/deleteComment")
    public Map<String, String> deleteComment(@PathVariable long id) {
        return commentService.deleteComment(id);
    }
}
