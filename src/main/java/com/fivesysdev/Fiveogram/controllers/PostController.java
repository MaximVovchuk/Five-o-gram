package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.CommentService;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
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
    public Map<String, String> addNewPost(@RequestParam @Nullable String text,
                                          MultipartFile multipartFile, @Null Long sponsorId) {
        return postService.save(text, multipartFile, sponsorId);
    }

    @GetMapping("/{id:\\d+}")
    public Post getPost(@PathVariable long id) {
        return postService.findPostById(id);
    }

    @PatchMapping("/{id:\\d+}/edit")
    public Map<String, String> editPost(@PathVariable long id, @RequestParam @Nullable String text, MultipartFile multipartFile) {
        return postService.editPost(id, text, multipartFile);
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public Map<String, String> deletePost(@PathVariable long id) {
        return postService.deletePost(id);
    }

    @PostMapping("/{id:\\d+}/addComment")
    public Map<String, String> addComment(@PathVariable long id, @RequestBody String text) {
        try {
            commentService.save(id, text);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message", "ok");
    }

    @PostMapping("/{id:\\d+}/setLike")
    public Map<String, String> addLike(@PathVariable long id) {
        return likeService.likePost(id);
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public Map<String, String> deleteLike(@PathVariable long id) {
        return likeService.unlikePost(id);
    }
    @GetMapping("/{id:\\d+}/getLikes")
    public Set<Like> getLikes(@PathVariable long id){
        return likeService.findAllPostLikes(id);
    }
}
