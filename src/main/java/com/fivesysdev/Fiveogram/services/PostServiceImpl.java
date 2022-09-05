package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.exceptions.NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.SponsorNotFoundException;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.PictureRepository;
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
import java.util.Objects;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SponsoredPostRepository sponsoredPostRepository;
    private final FileService fileService;
    private final PictureRepository pictureRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                           SponsoredPostRepository sponsoredPostRepository, FileService fileService, PictureRepository pictureRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
        this.fileService = fileService;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }


    @Override
    public ResponseEntity<Post> save(String text, MultipartFile multipartFile, Long sponsorId) {
        Post post = createAndSavePost(text, multipartFile);
        if (sponsorId != null) {
            User sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new SponsorNotFoundException();
            }
            createAndSaveSponsoredPost(post, sponsor);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    private void createAndSaveSponsoredPost(Post post, User sponsor) {
        SponsoredPost sponsoredPost = new SponsoredPost();
        sponsoredPost.setPost(post);
        sponsoredPost.setSponsor(sponsor);
        sponsoredPostRepository.save(sponsoredPost);
    }

    private Post createAndSavePost(String text, MultipartFile multipartFile) throws FileException{
        Post post = new Post();
        post.setAuthor(Context.getUserFromContext());
        post.setText(text);
        post.setPubDate(LocalDateTime.now());
        postRepository.save(post);
        String uri;
        if (multipartFile != null) {
            uri = fileService.saveFile(multipartFile);
            Picture picture = new Picture();
            picture.setPost(post);
            picture.setPath(uri);
            post.addPicture(picture);
            pictureRepository.save(picture);
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
    public ResponseEntity<Post> editPost(long id, String text, MultipartFile multipartFile) {
        Post oldPost = postRepository.findPostById(id);
        if (oldPost == null) {
            throw new PostNotFoundException();
        }
        if (!Objects.equals(oldPost.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            throw new NotYourPostException();
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uri = fileService.saveFile(multipartFile);
            Picture picture = new Picture();
            picture.setPath(uri);
            picture.setPost(oldPost);
            oldPost.addPicture(picture);
        }
        oldPost.setText(text);
        return new ResponseEntity<>(oldPost, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Post>> deletePost(long id) {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        if (!Objects.equals(post.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            throw new NotYourPostException();
        }
        if(sponsoredPostRepository.existsByPost(post)){
            sponsoredPostRepository.deleteByPost(post);
        }
        postRepository.deleteById(id);
        return new ResponseEntity<>(postRepository.findAllByAuthor(post.getAuthor()), HttpStatus.OK);
    }
}