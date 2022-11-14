package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.MarkDTO;
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
import java.util.Objects;

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
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
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
        Post post = createAndSavePost(user, postDTO.getText(), multipartFiles);
        if (sponsorId != null) {
            createAndSaveSponsoredPost(post, sponsor);
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
    public Post editPost(String username, PostDTO postDTO, long id)
            throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException{
        Post post = postRepository.findPostById(id);
        List<MultipartFile> multipartFiles = postDTO.getMultipartFiles();
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
        deleteMarksByPost(post);
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

    @Override
    public Post addMarks(String username, List<MarkDTO> markDTOs) throws Status449PictureNotFoundException, Status433NotYourPostException {
        Post post = null;
        deleteMarksByMarkDTOs(markDTOs);
        for (MarkDTO markDTO : markDTOs) {
            Picture picture = pictureRepository.findById(markDTO.getPhotoId())
                    .orElseThrow(Status449PictureNotFoundException::new);
            if (!Objects.equals(picture.getPost().getAuthor().getUsername(), username)) {
                throw new Status433NotYourPostException();
            }
            post = post == null ? picture.getPost() : null;
            markRepository.save(
                    Mark.builder()
                            .width(markDTO.getWidth())
                            .height(markDTO.getHeight())
                            .username(markDTO.getUsername())
                            .picture(picture)
                            .build());
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

    private void deleteMarksByPost(Post post) {
        for (Picture picture : post.getPictures()) {
            markRepository.deleteByPicture(picture);
        }
    }

    private void deleteMarksByMarkDTOs(List<MarkDTO> markDTOs){
        List<Picture> pictures = markDTOs.stream()
                .map(markDTO -> pictureRepository.findById(markDTO.getPhotoId()).orElseThrow()).toList();
        for (Picture picture : pictures) {
            markRepository.deleteByPicture(picture);
        }
    }

    private void deletePictures(List<Picture> pictures) {
        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
            fileService.deleteFile(picture.getPath());
        }
    }
}