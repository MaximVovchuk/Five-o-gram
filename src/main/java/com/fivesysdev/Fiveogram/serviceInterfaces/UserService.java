package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status442NoRecommendationPostsException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User findUserById(long id) throws Status437UserNotFoundException;

    User setAvatar(String username,MultipartFile multipartFile) throws Status441FileIsNullException;

    List<Post> getRecommendations(String username) throws Status442NoRecommendationPostsException;

    List<User> getFriendsList(String username);

    User editMe(String username, UserDTO userDTO);

    User findUserByUsername(String username);
}
