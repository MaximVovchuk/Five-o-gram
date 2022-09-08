package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    ResponseEntity<User> findUserById(long id) throws UserNotFoundException;

    ResponseEntity<User> setAvatar(MultipartFile multipartFile) throws FileException;

    ResponseEntity<List<Post>> getRecommendations();

    List<User> getFriendsList();

}
