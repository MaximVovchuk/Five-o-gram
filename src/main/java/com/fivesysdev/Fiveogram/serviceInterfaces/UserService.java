package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status442NoRecommendationPostsException;
import com.fivesysdev.Fiveogram.exceptions.Status447NotYourAvatarException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findUserById(long id) throws Status437UserNotFoundException;

    User setAvatar(String username, MultipartFile multipartFile) throws Status441FileIsNullException;

    List<Post> getRecommendations(String username) throws Status442NoRecommendationPostsException;

    List<User> getFriendsList(String username);

    User editMe(String username, UserDTO userDTO);

    User findUserByUsername(String username);

    List<User> searchByUsernameStartsWith(String text);

    Set<Post> getPostsWhereImMarked(String username);

    void deleteAvatar(String username, long id) throws Status447NotYourAvatarException;

    List<User> getUserSubscriptions(long id);

    List<User> getUserSubs(long id);
}
