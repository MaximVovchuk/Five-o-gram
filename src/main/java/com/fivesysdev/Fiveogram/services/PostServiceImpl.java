package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.MarkDTO;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.models.notifications.MarkNotification;
import com.fivesysdev.Fiveogram.repositories.*;
import com.fivesysdev.Fiveogram.serviceInterfaces.*;
import com.fivesysdev.Fiveogram.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SponsoredPostRepository sponsoredPostRepository;
    private final PictureRepository pictureRepository;
    private final MarkRepository markRepository;
    private final FileService fileService;
    private final HashtagService hashtagService;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }

    @Override
    public Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status437UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        List<MultipartFile> multipartFiles = postDTO.multipartFiles();
        if (multipartFiles==null || multipartFiles.isEmpty()) {
            throw new Status443DidNotReceivePictureException();
        }
        Long sponsorId = postDTO.sponsorId();
        User sponsor = null;
        if (sponsorId != null) {
            sponsor = userRepository.findUserById(sponsorId);
            if (sponsor == null) {
                throw new Status436SponsorNotFoundException();
            }
        }
        Post post = createAndSavePost(user, postDTO.text(), multipartFiles);
        hashtagService.saveAllHashtagsFromPost(post);
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
            throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            throw new Status435PostNotFoundException();
        }
        User user = userRepository.findUserByUsername(username);
        if (!post.getAuthor().equals(user)) {
            throw new Status433NotYourPostException();
        }
        List<MultipartFile> multipartFiles = postDTO.multipartFiles();
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
        post.setText(postDTO.text());
        hashtagService.deleteAllHashtagsFromPost(post);
        hashtagService.saveAllHashtagsFromPost(post);
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
    public void banPost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post addMarks(String username, List<MarkDTO> markDTOs)
            throws Status449PictureNotFoundException, Status433NotYourPostException,
            Status437UserNotFoundException {
        Post post = null;
        deleteMarksByMarkDTOs(markDTOs);
        for (MarkDTO markDTO : markDTOs) {
            Picture picture = pictureRepository.findById(markDTO.photoId())
                    .orElseThrow(Status449PictureNotFoundException::new);
            if (!username.equals(picture.getPost().getAuthor().getUsername())) {
                throw new Status433NotYourPostException();
            }
            if (!userRepository.existsByUsername(markDTO.username())) {
                throw new Status437UserNotFoundException();
            }
            post = post == null ? picture.getPost() : null;
            Mark mark = Mark.builder()
                    .width(markDTO.width())
                    .height(markDTO.height())
                    .username(markDTO.username())
                    .picture(picture)
                    .build();
            notificationService.sendNotification(
                    new MarkNotification(picture.getPost(), userRepository.findUserByUsername(username)));
            markRepository.save(mark);
        }
        return post;
    }

    @Override
    public Set<Post> getRecommendations(String username) {
        User user = userRepository.findUserByUsername(username);
        Set<Post> resultSet = new HashSet<>();
        List<List<String>> RawHashtags = likeRepository.findAllByWhoLikes(user).stream().map(Like::getPost)
                .map(post -> post.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList())).toList();
        Set<String> hashtags = new HashSet<>();
        for (List<String> rawHashtag : RawHashtags) {
            hashtags.addAll(rawHashtag);
        }
        for (String hashtag : hashtags) {
            List<Post> posts = hashtagService.getPostsByHashtags(List.of(hashtag));
            resultSet.add(posts.stream().max(Comparator.comparingInt(post -> post.getLikesList().size())).orElseThrow());
        }
        return resultSet;
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
        checkForMarksInTextAndSendNotifications(post);
        return post;
    }

    private void checkForMarksInTextAndSendNotifications(Post post) {
        String[] texts = post.getText().split("@");
        List<String> marks = StringUtil.get(texts);
        for (String mark : marks) {
            User user = userRepository.findUserByUsername(mark);
            if (user != null) {
                notificationService.sendNotification(
                        new MarkNotification(post, user));
            }
        }
    }

    private void deleteMarksByPost(Post post) {
        for (Picture picture : post.getPictures()) {
            markRepository.deleteByPicture(picture);
        }
    }

    private void deleteMarksByMarkDTOs(List<MarkDTO> markDTOs) throws Status449PictureNotFoundException {
        try {
            Set<Picture> pictures = markDTOs.stream()
                    .map(markDTO -> pictureRepository.findById(markDTO.photoId()).orElseThrow()).collect(Collectors.toSet());
            for (Picture picture : pictures) {
                markRepository.deleteByPicture(picture);
            }
        } catch (NoSuchElementException e) {
            throw new Status449PictureNotFoundException();
        }
    }
    private void deletePictures(List<Picture> pictures) {
        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
            fileService.deleteFile(picture.getPath());
        }
    }
}