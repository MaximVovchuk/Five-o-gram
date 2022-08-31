package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.models.Like;
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
import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<Map<String, String>> addNewPost(@RequestParam @Nullable String text,
                                                          MultipartFile multipartFile, @Null Long sponsorId) {
        return postService.save(text, multipartFile, sponsorId);
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
    public ResponseEntity<Map<String, String>> editPost(@PathVariable long id,
                                                        @RequestParam @Nullable String text, MultipartFile multipartFile) {
        try {
            return postService.editPost(id, text, multipartFile);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable long id) {
        try {
            return postService.deletePost(id);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<Map<String, String>> addComment(@PathVariable long id, @RequestParam String text) {
        try {
            commentService.save(id, text);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<Map<String, String>> addLike(@PathVariable long id) {
        try {
            return likeService.likePost(id);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<Map<String, String>> deleteLike(@PathVariable long id) {
        try {
            return likeService.unlikePost(id);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<Set<Like>> getLikes(@PathVariable long id) {
        try {
            return likeService.findAllPostLikes(id);
        } catch (PostNotFoundException ex) {
            return new ResponseEntity<>(ex.httpStatus);
        }
    }
}
