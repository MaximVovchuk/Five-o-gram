package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.PostRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SponsoredPostRepository sponsoredPostRepository;
    private final FileService fileService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                           SponsoredPostRepository sponsoredPostRepository, FileService fileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
        this.fileService = fileService;
    }

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }


    @Override
    public Map<String, String> save(String text, MultipartFile multipartFile, Long sponsorId) {
        if (text == null || text.isEmpty()) {
            return Map.of("Message", "Text is empty");
        }
        Post post = createPost(text, multipartFile);
        if (sponsorId != null) {
            User sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                return Map.of("Message", "Sponsor not found");
            }
            createAndSaveSponsoredPost(post, sponsor);
        }
        postRepository.save(post);
        return Map.of("Message", "ok");
    }

    private void createAndSaveSponsoredPost(Post post, User sponsor) {
        SponsoredPost sponsoredPost = new SponsoredPost();
        sponsoredPost.setPost(post);
        sponsoredPost.setSponsor(sponsor);
        sponsoredPostRepository.save(sponsoredPost);
    }

    private Post createPost(String text, MultipartFile multipartFile) {
        Post post = new Post();
        post.setAuthor(Context.getUserFromContext());
        post.setText(text);
        post.setPubDate(LocalDateTime.now());
        Picture picture;
        if (multipartFile == null) {
            post.setPicture(null);
        } else {
            picture = fileService.saveFile(multipartFile);
            post.setPicture(picture);
        }
        return post;
    }

    @Override
    public Post findPostById(long id) {
        return postRepository.findPostById(id);
    }

    @Override
    public Map<String, String> editPost(long id, String text, MultipartFile multipartFile) {
        if (text == null || text.isEmpty()) {
            return Map.of("Message", "Text is empty");
        }
        Post oldPost = postRepository.findPostById(id);
        if (oldPost == null) {
            return Map.of("Message", "Post not found");
        }
        if (!Objects.equals(oldPost.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            return Map.of("Message", "That`s not your post");
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            oldPost.setPicture(fileService.saveFile(multipartFile));
        }
        oldPost.setText(text);
        return Map.of("Message", "ok");
    }

    @Override
    public Map<String,String> deletePost(long id) {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            return Map.of("Message", "Post not found");
        }
        if (!Objects.equals(post.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            return Map.of("Message", "That`s not your post");
        }
        postRepository.deleteById(id);
        return Map.of("Message","ok");
    }


}