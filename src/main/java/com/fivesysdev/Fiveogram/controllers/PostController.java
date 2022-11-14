package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.MarksToAddDTO;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Post> addNewPost(@ModelAttribute PostDTO postDTO,
                                           @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException, Status436SponsorNotFoundException,
            Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status437UserNotFoundException {
        return ResponseEntity.ok(postService.save(jwtUtil.getUsername(token), postDTO));
    }

    @PostMapping("/addMarks")
    public ResponseEntity<Post> addMarks(@RequestBody MarksToAddDTO marksToAddDTO,
                                         @RequestHeader(value = "Authorization") String token)
            throws Status449PictureNotFoundException, Status433NotYourPostException {
        return ResponseEntity.ok(postService.addMarks(jwtUtil.getUsername(token), marksToAddDTO.getMarkDTOs()));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Post> getPost(@PathVariable long id) throws Status435PostNotFoundException {
        return ResponseEntity.ok(postService.findPostById(id));
    }

    @PatchMapping("/{id:\\d+}/edit")
    public ResponseEntity<Post> editPost(@PathVariable long id, @ModelAttribute PostDTO postDTO,
                                         @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException, Status435PostNotFoundException,
            Status433NotYourPostException, Status437UserNotFoundException, Status446MarksBadRequestException {
        return ResponseEntity.ok(postService.editPost(jwtUtil.getUsername(token), postDTO, id));
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public ResponseEntity<List<Post>> deletePost(@PathVariable long id,
                                                 @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status433NotYourPostException {
        return ResponseEntity.ok(postService.deletePost(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id:\\d+}/addComment")
    public ResponseEntity<Comment> addComment(@PathVariable long id, @RequestParam @Nullable String text,
                                              @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status448TextIsNullException {
        return ResponseEntity.ok(commentService.save(jwtUtil.getUsername(token), id, text));
    }

    @PostMapping("/{id:\\d+}/setLike")
    public ResponseEntity<Post> addLike(@PathVariable long id,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status438PostAlreadyLikedException,
            Status435PostNotFoundException {
        return ResponseEntity.ok(likeService.likePost(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public ResponseEntity<Post> deleteLike(@PathVariable long id,
                                           @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException {
        return ResponseEntity.ok(likeService.unlikePost(jwtUtil.getUsername(token), id));
    }

    @GetMapping("/{id:\\d+}/getLikes")
    public ResponseEntity<Set<Like>> getLikes(@PathVariable long id) throws Status435PostNotFoundException {
        return ResponseEntity.ok(likeService.findAllPostLikes(id));
    }

    @PostMapping("/{id:\\d+}/report")
    public ResponseEntity<Post> report(@PathVariable long id, @RequestBody String text)
            throws Status435PostNotFoundException {
        return ResponseEntity.ok(postService.reportPost(text, id));
    }
}