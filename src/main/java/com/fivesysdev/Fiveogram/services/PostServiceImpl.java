package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status433NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status436SponsorNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<Post> save(String username,String text, List<MultipartFile> multipartFiles, Long sponsorId) throws Status441FileException, Status436SponsorNotFoundException {
        User user = userRepository.findUserByUsername(username);
        User sponsor = null;
        if (sponsorId != null) {
            sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new Status436SponsorNotFoundException();
            }
        }
        Post post = createAndSavePost(user,text, multipartFiles);
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

    private Post createAndSavePost(User user,String text, List<MultipartFile> multipartFiles) throws Status441FileException {
        Post post = new Post();
        post.setAuthor(user);
        post.setText(text);
        post.setPubDate(LocalDateTime.now());
        postRepository.save(post);
        String uri;
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                uri = fileService.saveFile(user,multipartFile);
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
    public Post findPostById(long id) throws Status435PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        return post;
    }

    @Override
    public ResponseEntity<Post> editPost(String username,long id,
                                         String text, List<MultipartFile> multipartFiles)
            throws Status441FileException, Status433NotYourPostException, Status435PostNotFoundException {
        Post oldPost = postRepository.findPostById(id);
        if (oldPost == null) {
            throw new Status435PostNotFoundException();
        }
        User user = userRepository.findUserByUsername(username);
        if (oldPost.getAuthor().equals(user)) {
            throw new Status433NotYourPostException();
        }
        deletePictures(oldPost.getPictures());
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                String uri = fileService.saveFile(user,multipartFile);
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
    public ResponseEntity<List<Post>> deletePost(String username,long id) throws Status433NotYourPostException, Status435PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        if (!post.getAuthor().equals(userRepository.findUserByUsername(username))) {
            throw new Status433NotYourPostException();
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