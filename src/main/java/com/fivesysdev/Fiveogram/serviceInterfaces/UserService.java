package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<User> findUserById(long id);

    ResponseEntity<Map<String, String>> setAvatar(MultipartFile multipartFile);

    ResponseEntity<List<Post>> getRecommendations();

    List<User> getFriendsList();

}
