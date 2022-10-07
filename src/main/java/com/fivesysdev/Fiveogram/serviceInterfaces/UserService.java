package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    ResponseEntity<User> findUserById(long id) throws Status437UserNotFoundException;

    ResponseEntity<User> setAvatar(String username,MultipartFile multipartFile) throws Status441FileException;

    ResponseEntity<List<Post>> getRecommendations(String username);

    List<User> getFriendsList(String username);

    ResponseEntity<?> editMe(String username, UserDTO userDTO);

    User findUserByUsername(String username);
}
