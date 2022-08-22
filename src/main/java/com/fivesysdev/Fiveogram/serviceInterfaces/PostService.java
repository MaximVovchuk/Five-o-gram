package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    List<Post> findAll();

    Post save(String name, MultipartFile multipartFile,Long sponsorId) throws UserNotFoundException;

    List<Post> findRecommendations(User user);
    Post findPostById(long id);
    boolean editPost(long id, String text, MultipartFile multipartFile);
    void deletePost(long id);

    boolean addLike(Like like);

    boolean unlikePost(long id);

    //Post sponsorSave(String text, MultipartFile multipartFile, Long sponsorId) throws UserNotFoundException;
}
