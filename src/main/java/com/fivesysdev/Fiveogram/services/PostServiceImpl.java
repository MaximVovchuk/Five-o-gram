package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.PictureRepository;
import com.fivesysdev.Fiveogram.repositories.PostRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.util.Context;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SponsoredPostRepository sponsoredPostRepository;
    private final FileService fileService;

    public PostServiceImpl(PostRepository postRepository, PictureRepository pictureRepository,
                           ModelMapper modelMapper, UserRepository userRepository,
                           SponsoredPostRepository sponsoredPostRepository, FileService fileService) {
        this.postRepository = postRepository;
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
        this.fileService = fileService;
    }

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post save(String text, MultipartFile multipartFile, Long sponsorId) throws UserNotFoundException {
        Post post = new Post();
        post.setAuthor(Context.getUserFromContext());
        post.setText(text);
        post.setPubDate(LocalDate.now());
        Picture picture;
        if (multipartFile == null) {
            post.setPicture(null);
        } else {
            picture = fileService.saveFile(multipartFile);
            post.setPicture(picture);
        }
        if (sponsorId != null) {
            User sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new UserNotFoundException();
            }
            SponsoredPost sponsoredPost = new SponsoredPost();
            sponsoredPost.setPost(post);
            sponsoredPost.setSponsor(sponsor);
            sponsoredPostRepository.save(sponsoredPost);
        }
        return postRepository.save(post);
    }

    @Override
    public List<Post> findRecommendations(User user) {
        return postRepository.findAllByAuthor(user).stream()
                .flatMap(post -> post.getCommentList().stream())
                .map(Comment::getAuthor)
                .flatMap(commentAuthor -> postRepository.findAllByAuthor(commentAuthor).stream().limit(5))
                .collect(Collectors.toList());
    }

    @Override
    public Post findPostById(long id) {
        return postRepository.findPostById(id);
    }

    @Override
    public boolean editPost(long id, String text, MultipartFile multipartFile) {
        Post oldPost = postRepository.findPostById(id);
        if (!multipartFile.isEmpty()) {
            oldPost.setPicture(fileService.saveFile(multipartFile));
        }
        if (!text.isEmpty() && !text.equals(" ")) {
            oldPost.setText(text);
        }
        return true;
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public boolean addLike(Like like) {
        if (!like.getPost().getLikesList().contains(like)) {
            like.getPost().addLike(like);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlikePost(long id) {
        Post post = findPostById(id);
        User user = Context.getUserFromContext();
        Like like = new Like(post, user);
        if (post.getLikesList().contains(like)) {
            post.getLikesList().remove(like);
            return true;
        }
        return false;
    }


    private Post convertToPost(PostDTO postDTO) {
        return this.modelMapper.map(postDTO, Post.class);
    }
}