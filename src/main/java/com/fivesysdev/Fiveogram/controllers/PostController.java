package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.facadeInterfaces.CommentFacade;
import com.fivesysdev.Fiveogram.facadeInterfaces.LikeFacade;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    private final ModelMapper modelMapper;
    private final PostService postService;
    private final CommentFacade commentFacade;
    private final LikeService likeService;
    private final LikeFacade likeFacade;

    public PostController(ModelMapper modelMapper, PostService postService, CommentFacade commentFacade, LikeService likeService, LikeFacade likeFacade) {
        this.modelMapper = modelMapper;
        this.postService = postService;
        this.commentFacade = commentFacade;
        this.likeService = likeService;
        this.likeFacade = likeFacade;
    }

    @PostMapping("/newPost")
    public Map<String, String> addNewPost(@RequestParam String text,
                                          MultipartFile multipartFile, @Null Long sponsorId) {
        try {
            postService.save(text, multipartFile, sponsorId);
        } catch (UserNotFoundException ex) {
            return Map.of("Message", "Sponsor not found");
        }
        return Map.of("Message", "ok");
    }

    @GetMapping("/{id:\\d+}")
    public Post getPost(@PathVariable long id) {
        return postService.findPostById(id);
    }

    @PatchMapping("/{id:\\d+}/edit")
    public Map<String, String> editPost(@PathVariable long id, @RequestParam String text, MultipartFile multipartFile) {
        return postService.editPost(id, text, multipartFile) ?
                Map.of("Message", "ok") : Map.of("Message", "IOException");
    }

    @DeleteMapping("/{id:\\d+}/delete")
    public Map<String, String> deletePost(@PathVariable long id) {
        try {
            postService.deletePost(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message", "ok");
    }

    @PostMapping("/{id:\\d+}/addComment")
    public Map<String, String> addComment(@PathVariable long id, @RequestBody String text) {
        try {
            commentFacade.addComment(id, text);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message", "ok");
    }

    @PostMapping("/{id:\\d+}/setLike")
    public Map<String, String> addLike(@PathVariable long id) {
        try {
            likeFacade.likePost(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message", "ok");
    }

    @PostMapping("/{id:\\d+}/deleteLike")
    public Map<String, String> deleteLike(@PathVariable long id) {
        try {
            likeService.unlikePost(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Map.of("Message", "Error!");
        }
        return Map.of("Message", "ok");
    }

    public Post convertToPost(PostDTO postDTO) {
        Post post = this.modelMapper.map(postDTO, Post.class);
        post.setAuthor(Context.getUserFromContext());
        return post;
    }
}
