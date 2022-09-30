package com.fivesysdev.Fiveogram.controllers;

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

    public PostController(PostService postService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostMapping("/newPost")
    public ResponseEntity<?> addNewPost(@ModelAttribute PostDTO postDTO) throws Status441FileException, Status436SponsorNotFoundException {
        return postService.save(postDTO.getText(), postDTO.getMultipartFiles(), postDTO.getSponsorId());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Post> getPost(@PathVariable long id) throws Status435PostNotFoundException {
        return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id:\\d+}/edit")
    public ResponseEntity<?> editPost(@PathVariable long id, @ModelAttribute PostDTO postDTO) throws Status441FileException, Status435PostNotFoundException, Status433NotYourPostException {
        return postService.editPost(id, postDTO.getText(), postDTO.getMultipartFiles());
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<?> deletePost(@PathVariable long id) throws Status435PostNotFoundException, Status433NotYourPostException {
        return postService.deletePost(id);
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<?> addComment(@PathVariable long id, @RequestParam String text) throws Status435PostNotFoundException {
        return new ResponseEntity<>(commentService.save(id, text), HttpStatus.OK);
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<?> addLike(@PathVariable long id) throws Status437UserNotFoundException, Status438PostAlreadyLikedException, Status435PostNotFoundException {
        return likeService.likePost(id);
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<?> deleteLike(@PathVariable long id) throws Status435PostNotFoundException {
        return likeService.unlikePost(id);
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<?> getLikes(@PathVariable long id) throws Status435PostNotFoundException {
        return likeService.findAllPostLikes(id);
    }
}
