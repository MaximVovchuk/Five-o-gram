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
import com.fivesysdev.Fiveogram.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
@Api(value = "Comment endpoints", tags = {"Comment"})
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final JWTUtil jwtUtil;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
        this.jwtUtil = jwtUtil;
    }

    @PatchMapping("/{id}/edit")
    public Response<Comment> editComment(@PathVariable long id, @RequestParam String text,
                                               @ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status435PostNotFoundException,
            Status434CommentNotFoundException, Status432NotYourCommentException {
        return new Response<>(commentService.editComment(jwtUtil.getUsername(token), id, text));
    }

    @DeleteMapping("/{id}/delete")
    public Response<Post> deleteComment(@PathVariable long id,
                                              @ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status435PostNotFoundException,
            Status434CommentNotFoundException, Status432NotYourCommentException {
        return new Response<>(commentService.deleteComment(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id}/setLike")
    public Response<Post> setLike(@PathVariable long id,
                                        @ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token)
            throws Status434CommentNotFoundException {
        return new Response<>(commentLikeService.setLike(jwtUtil.getUsername(token), id));
    }

    @DeleteMapping("{id}/deleteLike")
    public Response<Post> deleteLike(@PathVariable long id,
                                           @ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token)
            throws Status434CommentNotFoundException {
        return new Response<>(commentLikeService.deleteLike(jwtUtil.getUsername(token), id));
    }
}
