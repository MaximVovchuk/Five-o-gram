package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/editComment")
    public Map<String,String> editComment(@PathVariable long id,String text){
        try {
            commentService.editComment(id,text);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message","ok");
    }
    @PostMapping("/{id}/deleteComment")
    public Map<String,String> deleteComment(@PathVariable long id){
        try {
            commentService.deleteComment(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message","ok");
    }
}
