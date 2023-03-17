package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.HashtagService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final HashtagService hashtagService;
    private final PostService postService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/byHashtags")
    public Response<List<Post>> searchByHashtags(@RequestBody List<String> hashtags) {
        return new Response<>(hashtagService.getPostsByHashtags(hashtags));
    }

    @GetMapping("/byUsername")
    public Response<?> search(@RequestParam String startsWith) {
        return new Response<>(userService.searchByUsernameStartsWith(startsWith));
    }

    @GetMapping()
    public Response<Set<Post>> getMyRecommendations(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(postService.getRecommendations(jwtUtil.getUsername(token)));
    }
}
