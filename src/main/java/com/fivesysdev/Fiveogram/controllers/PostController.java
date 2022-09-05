package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    public PostController(PostService postService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostMapping("/newPost")
    public ResponseEntity<?> addNewPost(@RequestParam @Nullable String text,
                                        MultipartFile multipartFile, @Null Long sponsorId) {
        try {
            return postService.save(text, multipartFile, sponsorId);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Post> getPost(@PathVariable long id) {
        try {
            return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @PatchMapping("/{id:\\d+}/edit")
    public ResponseEntity<?> editPost(@PathVariable long id,
                                      @RequestParam @Nullable String text, MultipartFile multipartFile) {
        try {
            return postService.editPost(id, text, multipartFile);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        try {
            return postService.deletePost(id);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<?> addComment(@PathVariable long id, @RequestParam String text) {
        try {
            return new ResponseEntity<>(commentService.save(id, text), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<?> addLike(@PathVariable long id) {
        try {
            return likeService.likePost(id);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<?> deleteLike(@PathVariable long id) {
        try {
            return likeService.unlikePost(id);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<?> getLikes(@PathVariable long id) {
        try {
            return likeService.findAllPostLikes(id);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
