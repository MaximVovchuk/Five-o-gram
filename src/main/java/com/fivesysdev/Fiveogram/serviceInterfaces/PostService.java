package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {
    List<Post> findAll(User user);

    ResponseEntity<Map<String, String>> save(String name, MultipartFile multipartFile, Long sponsorId);

    Post findPostById(long id);
    ResponseEntity<Map<String, String>>editPost(long id, String text, MultipartFile multipartFile);
    ResponseEntity<Map<String,String>> deletePost(long id);


}
