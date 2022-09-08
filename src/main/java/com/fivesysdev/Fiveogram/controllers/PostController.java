package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
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

    public PostController(PostService postService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostMapping("/newPost")
    public ResponseEntity<?> addNewPost(@ModelAttribute PostDTO postDTO) {
        try {
            return postService.save(postDTO.getText(), postDTO.getMultipartFiles(), postDTO.getSponsorId());
        } catch (Exception ex) {
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
    public ResponseEntity<?> editPost(@PathVariable long id, @ModelAttribute PostDTO postDTO) {
        try {
            return postService.editPost(id, postDTO.getText(), postDTO.getMultipartFiles());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        try {
            return postService.deletePost(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<?> addComment(@PathVariable long id, @RequestParam String text) {
        try {
            return new ResponseEntity<>(commentService.save(id, text), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<?> addLike(@PathVariable long id) {
        try {
            return likeService.likePost(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<?> deleteLike(@PathVariable long id) {
        try {
            return likeService.unlikePost(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<?> getLikes(@PathVariable long id) {
        try {
            return likeService.findAllPostLikes(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
