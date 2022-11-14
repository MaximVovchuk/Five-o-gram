package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.dto.PostResponseDTO;
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
import java.util.ArrayList;
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
                           PictureRepository pictureRepository,
                           MarkRepository markRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.reportPostRepository = reportPostRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
        this.fileService = fileService;
        this.pictureRepository = pictureRepository;
        this.markRepository = markRepository;
    }

    @Override
    public List<PostResponseDTO> findAll(User user) {
        List<Post> posts = postRepository.findAllByAuthor(user);
        List<Mark> marks = new ArrayList<>();
        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();
        for (Post post : posts) {
            for (Picture picture : post.getPictures()) {
                marks.addAll(markRepository.findAllByPicture(picture));
            }
            PostResponseDTO postResponseDTO = new PostResponseDTO(post, marks);
            postResponseDTOs.add(postResponseDTO);
        }
        return postResponseDTOs;
    }

    @Override
    public Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status437UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        List<MultipartFile> multipartFiles = postDTO.getMultipartFiles();
        if (multipartFiles.isEmpty()) {
            throw new Status443DidNotReceivePictureException();
        }
        Long sponsorId = postDTO.getSponsorId();
        User sponsor = null;
        if (sponsorId != null) {
            sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new Status436SponsorNotFoundException();
            }
        }
        checkMarks(postDTO);
        Post post = createAndSavePost(user, postDTO.getText(), multipartFiles);
        saveMarks(postDTO, post);
        if (sponsorId != null) {
            createAndSaveSponsoredPost(post, sponsor);
        }
        return post;
    }


    @Override
    public PostResponseDTO findPostById(long id) throws Status435PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        List<Mark> marks = new ArrayList<>();
        for (Picture picture : post.getPictures()) {
            marks.addAll(markRepository.findAllByPicture(picture));
        }
        return new PostResponseDTO(post, marks);
    }

    @Override
    public Post editPost(String username, PostDTO postDTO, long id)
            throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException, Status437UserNotFoundException, Status446MarksBadRequestException {
        Post post = postRepository.findPostById(id);
        List<MultipartFile> multipartFiles = postDTO.getMultipartFiles();
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        checkMarks(postDTO);
        deleteMarks(post);
        saveMarks(postDTO, post);
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
        post.setText(postDTO.getText());
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

    private void deleteMarks(Post post) {
        for (Picture picture : post.getPictures()) {
            markRepository.deleteByPicture(picture);
        }
    }

    private void checkMarks(PostDTO postDTO) throws Status446MarksBadRequestException, Status437UserNotFoundException {
        if (postDTO.getHeights() == null || postDTO.getPhotosCount() == null
                || postDTO.getWidths() == null || postDTO.getUsernames() == null
                || postDTO.getHeights().size() != postDTO.getWidths().size()
                || postDTO.getWidths().size() != postDTO.getUsernames().size()
                || postDTO.getUsernames().size() != postDTO.getPhotosCount().size()
                || postDTO.getPhotosCount().stream().anyMatch(i -> i > postDTO.getMultipartFiles().size())) {
            throw new Status446MarksBadRequestException();
        }
        for (String name : postDTO.getUsernames()) {
            if (!userRepository.existsByUsername(name)) {
                throw new Status437UserNotFoundException();
            }
        }
    }

    private void deletePictures(List<Picture> pictures) {
        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
            fileService.deleteFile(picture.getPath());
        }
    }
}