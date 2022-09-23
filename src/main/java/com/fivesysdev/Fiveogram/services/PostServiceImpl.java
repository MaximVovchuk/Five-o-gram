package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status403NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404SponsorNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status408FileException;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.SponsoredPost;
import com.fivesysdev.Fiveogram.models.User;
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
    public ResponseEntity<Post> save(String text, List<MultipartFile> multipartFiles, Long sponsorId) throws Status408FileException, Status404SponsorNotFoundException {
        User sponsor = null;
        if (sponsorId != null) {
            sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new Status404SponsorNotFoundException();
            }
        }
        Post post = createAndSavePost(text, multipartFiles);
        if (sponsorId != null) {
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

    private Post createAndSavePost(String text, List<MultipartFile> multipartFiles) throws Status408FileException {
        Post post = new Post();
        post.setAuthor(Context.getUserFromContext());
        post.setText(text);
        post.setPubDate(LocalDateTime.now());
        postRepository.save(post);
        String uri;
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                uri = fileService.saveFile(multipartFile);
                Picture picture = new Picture();
                picture.setPost(post);
                picture.setPath(uri);
                post.addPicture(picture);
                pictureRepository.save(picture);
            }
        }
        return post;
    }

    @Override
    public Post findPostById(long id) throws Status404PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status404PostNotFoundException();
        }
        return post;
    }

    @Override
    public ResponseEntity<Post> editPost(long id, String text, List<MultipartFile> multipartFiles) throws Status408FileException, Status403NotYourPostException, Status404PostNotFoundException {
        Post oldPost = postRepository.findPostById(id);
        if (oldPost == null) {
            throw new Status404PostNotFoundException();
        }
        if (!Objects.equals(oldPost.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            throw new Status403NotYourPostException();
        }
        deletePictures(oldPost.getPictures());
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                String uri = fileService.saveFile(multipartFile);
                Picture picture = new Picture();
                picture.setPath(uri);
                picture.setPost(oldPost);
                oldPost.addPicture(picture);
            }
        }
        oldPost.setText(text);
        return new ResponseEntity<>(oldPost, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Post>> deletePost(long id) throws Status403NotYourPostException, Status404PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status404PostNotFoundException();
        }
        if (!Objects.equals(post.getAuthor(), userRepository.findUserById(Context.getUserFromContext().getId()))) {
            throw new Status403NotYourPostException();
        }
        if (sponsoredPostRepository.existsByPost(post)) {
            sponsoredPostRepository.deleteByPost(post);
        }
        deletePictures(post.getPictures());
        postRepository.deleteById(id);
        return new ResponseEntity<>(postRepository.findAllByAuthor(post.getAuthor()), HttpStatus.OK);
    }

    private void deletePictures(List<Picture> pictures) {
        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
            fileService.deleteFile(picture.getPath());
        }
    }
}