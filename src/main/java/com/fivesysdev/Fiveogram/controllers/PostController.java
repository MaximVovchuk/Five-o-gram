package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final JWTUtil jwtUtil;

    public PostController(PostService postService, CommentService commentService, LikeService likeService, JWTUtil jwtUtil) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/newPost")
    public ResponseEntity<?> addNewPost(@ModelAttribute PostDTO postDTO,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status441FileException, Status436SponsorNotFoundException {
        return postService.save(jwtUtil.validate(token)
                , postDTO.getText(), postDTO.getMultipartFiles(), postDTO.getSponsorId());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Post> getPost(@PathVariable long id) throws Status435PostNotFoundException {
        return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id:\\d+}/edit")
    public ResponseEntity<?> editPost(@PathVariable long id, @ModelAttribute PostDTO postDTO,
                                      @RequestHeader(value = "Authorization") String token)
            throws Status441FileException, Status435PostNotFoundException,
            Status433NotYourPostException {
        return postService.editPost(jwtUtil.validate(token),
                id, postDTO.getText(), postDTO.getMultipartFiles());
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<?> deletePost(@PathVariable long id,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status433NotYourPostException {
        return postService.deletePost(jwtUtil.validate(token),id);
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<?> addComment(@PathVariable long id, @RequestParam String text,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException {
        return new ResponseEntity<>(commentService.save(jwtUtil.validate(token),id, text), HttpStatus.OK);
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<?> addLike(@PathVariable long id,
                                     @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status438PostAlreadyLikedException,
            Status435PostNotFoundException {
        return likeService.likePost(jwtUtil.validate(token),id);
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<?> deleteLike(@PathVariable long id,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException {
        return likeService.unlikePost(jwtUtil.validate(token),id);
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<?> getLikes(@PathVariable long id) throws Status435PostNotFoundException {
        return likeService.findAllPostLikes(id);
    }
}
