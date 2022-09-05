package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    ResponseEntity<Post> save(String name, MultipartFile multipartFile, Long sponsorId);

    Post findPostById(long id);
    ResponseEntity<Post>editPost(long id, String text, MultipartFile multipartFile);
    ResponseEntity<List<Post>> deletePost(long id);


}
