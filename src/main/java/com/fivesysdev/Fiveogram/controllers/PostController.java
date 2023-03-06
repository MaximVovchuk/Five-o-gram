package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.MarksToAddDTO;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.*;
import com.fivesysdev.Fiveogram.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final ReportService reportService;
    private final LikeService likeService;
    private final JWTUtil jwtUtil;
    private final HashtagService hashtagService;

    @PostMapping("/newPost")
    public Response<Post> addNewPost(@ModelAttribute PostDTO postDTO,
                                           @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException, Status436SponsorNotFoundException,
            Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status437UserNotFoundException {
        return new Response<>(postService.save(jwtUtil.getUsername(token), postDTO));
    }

    @PostMapping("/addMarks")
    public Response<Post> addMarks(@RequestBody MarksToAddDTO marksToAddDTO,
                                         @RequestHeader(value = "Authorization") String token)
            throws Status449PictureNotFoundException, Status433NotYourPostException, Status437UserNotFoundException {
        return new Response<>(postService.addMarks(jwtUtil.getUsername(token), marksToAddDTO.getMarkDTOs()));
    }

    @GetMapping("/{id}")
    public Response<Post> getPost(@PathVariable long id) throws Status435PostNotFoundException {
        return new Response<>(postService.findPostById(id));
    }

    @PatchMapping("/{id}/edit")
    public Response<Post> editPost(@PathVariable long id, @ModelAttribute PostDTO postDTO,
                                         @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException, Status435PostNotFoundException,
            Status433NotYourPostException, Status437UserNotFoundException, Status446MarksBadRequestException {
        return new Response<>(postService.editPost(jwtUtil.getUsername(token), postDTO, id));
    }

    @DeleteMapping("/{id}/delete")
    public Response<List<Post>> deletePost(@PathVariable long id,
                                                 @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status433NotYourPostException {
        return new Response<>(postService.deletePost(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id}/addComment")
    public Response<Comment> addComment(@PathVariable long id, @RequestParam @Nullable String text,
                                              @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status448TextIsNullException {
        return new Response<>(commentService.save(jwtUtil.getUsername(token), id, text));
    }

    @PostMapping("/{id}/setLike")
    public Response<Post> addLike(@PathVariable long id,
                                        @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status438PostAlreadyLikedException,
            Status435PostNotFoundException {
        return new Response<>(likeService.likePost(jwtUtil.getUsername(token), id));
    }

    @PostMapping("/{id}/deleteLike")
    public Response<Post> deleteLike(@PathVariable long id,
                                           @RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException {
        return new Response<>(likeService.unlikePost(jwtUtil.getUsername(token), id));
    }

    @GetMapping("/{id}/getLikes")
    public Response<Set<Like>> getLikes(@PathVariable long id) throws Status435PostNotFoundException {
        return new Response<>(likeService.findAllPostLikes(id));
    }

    @PostMapping("/{id}/report")
    public Response<Post> report(@PathVariable long id, @RequestParam String text)
            throws Status435PostNotFoundException {
        return new Response<>(reportService.reportPost(id,text));
    }
    @GetMapping("/search")
    public Response<List<Post>> searchByHashtags(@RequestBody List<String> hashtags){
        return new Response<>(hashtagService.getPostsByHashtags(hashtags));
    }

    @GetMapping("/getRecommendations")
    public Response<Set<Post>> getMyRecommendations(@RequestHeader(value = "Authorization") String token){
        return new Response<>(postService.getRecommendations(jwtUtil.getUsername(token)));
    }
}