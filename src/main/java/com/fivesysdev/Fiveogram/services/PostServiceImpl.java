package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.PostRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> save(String text, MultipartFile multipartFile, Long sponsorId) {
        if (text == null || text.isEmpty()) {
            return new ResponseEntity<>(Map.of("Message", "Text is empty"), HttpStatus.BAD_REQUEST);
        }
        Post post = createPost(text, multipartFile);
        if (sponsorId != null) {
            User sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                return new ResponseEntity<>(Map.of("Message", "Sponsor not found"), HttpStatus.BAD_REQUEST);
            }
            createAndSaveSponsoredPost(post, sponsor);
        }
        postRepository.save(post);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
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
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return post;
    }

    @Override
    public ResponseEntity<Map<String, String>> editPost(long id, String text, MultipartFile multipartFile) {
        if (text == null || text.isEmpty()) {
            return new ResponseEntity<>(Map.of("Message", "Text is empty"), HttpStatus.BAD_REQUEST);
        }
        Post oldPost = postRepository.findPostById(id);
        if (oldPost == null) {
            throw new PostNotFoundException();
        }
        if (!Objects.equals(oldPost.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            return new ResponseEntity<>(Map.of("Message", "That`s not your post"), HttpStatus.BAD_REQUEST);
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            oldPost.setPicture(fileService.saveFile(multipartFile));
        }
        oldPost.setText(text);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> deletePost(long id) {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        if (!Objects.equals(post.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            return new ResponseEntity<>(Map.of("Message", "That`s not your post"), HttpStatus.BAD_REQUEST);
        }
        postRepository.deleteById(id);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }
}