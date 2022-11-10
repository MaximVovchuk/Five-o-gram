package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.models.reports.ReportPostEntity;
import com.fivesysdev.Fiveogram.repositories.*;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
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
    private final ReportPostRepository reportPostRepository;
    private final SponsoredPostRepository sponsoredPostRepository;
    private final PictureRepository pictureRepository;
    private final MarkRepository markRepository;
    private final FileService fileService;

    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           ReportPostRepository reportPostRepository,
                           SponsoredPostRepository sponsoredPostRepository,
                           FileService fileService,
                           PictureRepository pictureRepository, MarkRepository markRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.reportPostRepository = reportPostRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
        this.fileService = fileService;
        this.pictureRepository = pictureRepository;
        this.markRepository = markRepository;
    }

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }


    @Override
    public Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequest {
        User user = userRepository.findUserByUsername(username);
        String text = postDTO.getText();
        List<MultipartFile> multipartFiles = postDTO.getMultipartFiles();
        Long sponsorId = postDTO.getSponsorId();
        User sponsor = null;
        if (multipartFiles.isEmpty()) {
            throw new Status443DidNotReceivePictureException();
        }
        if (sponsorId != null) {
            sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new Status436SponsorNotFoundException();
            }
        }
            if (postDTO.getHeights().size() != postDTO.getWidths().size()
                || postDTO.getWidths().size() != postDTO.getUsernames().size()
                || postDTO.getUsernames().size() != postDTO.getPhotosCount().size()
                || postDTO.getPhotosCount().stream().anyMatch(i -> i > postDTO.getPhotosCount().size())) {
            throw new Status446MarksBadRequest();
        }
        Post post = createAndSavePost(user, text, multipartFiles);
        saveMarks(postDTO, post);
        if (sponsorId != null) {
            createAndSaveSponsoredPost(post, sponsor);
        }
        return post;
    }

    private void createAndSaveSponsoredPost(Post post, User sponsor) {
        SponsoredPost sponsoredPost = new SponsoredPost();
        sponsoredPost.setPost(post);
        sponsoredPost.setSponsor(sponsor);
        sponsoredPostRepository.save(sponsoredPost);
    }

    private Post createAndSavePost(User user, String text, List<MultipartFile> multipartFiles) throws Status441FileIsNullException {
        Post post = new Post();
        post.setAuthor(user);
        post.setText(text);
        post.setPubDate(LocalDateTime.now());
        postRepository.save(post);
        String uri;
        for (MultipartFile multipartFile : multipartFiles) {
            uri = fileService.saveFile(user, multipartFile);
            Picture picture = new Picture();
            picture.setPost(post);
            picture.setPath(uri);
            post.addPicture(picture);
            pictureRepository.save(picture);
        }

        return post;
    }

    private void saveMarks(PostDTO postDTO, Post post) {
        for (int i = 0; i < postDTO.getHeights().size(); i++) {
            Mark mark = new Mark();
            mark.setHeight(postDTO.getHeights().get(i));
            mark.setWidth(postDTO.getWidths().get(i));
            mark.setUsername(postDTO.getUsernames().get(i));
            mark.setPicture(post.getPictures().get(postDTO.getPhotosCount().get(i) - 1));
            markRepository.save(mark);
        }
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
    public Post editPost(String username, long id, String text, List<MultipartFile> multipartFiles)
            throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        User user = userRepository.findUserByUsername(username);
        if (post.getAuthor().equals(user)) {
            throw new Status433NotYourPostException();
        }
        deletePictures(post.getPictures());
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                String uri = fileService.saveFile(user, multipartFile);
                Picture picture = new Picture();
                picture.setPath(uri);
                picture.setPost(post);
                post.addPicture(picture);
            }
        }
        post.setText(text);
        return post;
    }

    @Override
    public List<Post> deletePost(String username, long id) throws Status433NotYourPostException, Status435PostNotFoundException {
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
        return postRepository.findAllByAuthor(post.getAuthor());
    }

    @Override
    public Post reportPost(String text, long id) throws Status435PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(Status435PostNotFoundException::new);
        reportPostRepository.save(ReportPostEntity.builder()
                .text(text)
                .post(post)
                .build());
        return post;
    }

    @Override
    public void banPost(Long id) {
        postRepository.deleteById(id);
    }

    private void deletePictures(List<Picture> pictures) {
        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
            fileService.deleteFile(picture.getPath());
        }
    }
}