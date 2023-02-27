package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findUserById(Long id) throws Status437UserNotFoundException;

    User setAvatar(String username, MultipartFile multipartFile) throws Status441FileIsNullException;

    List<Post> getFeed(String username) throws Status442NoFeedPostsException, Status437UserNotFoundException;

    List<User> getFriendsList(String username) throws Status437UserNotFoundException;

    String editMe(String username, UserDTO userDTO) throws Status439UsernameBusyException;

    User findUserByUsername(String username);

    List<User> searchByUsernameStartsWith(String text);

    Set<Post> getPostsWhereImMarked(String username);

    void deleteAvatar(String username, Long id) throws Status447NotYourAvatarException, Status450AvatarNotFoundException;

    List<User> getUserSubscriptions(Long id) throws Status437UserNotFoundException;

    List<User> getUserSubs(Long id) throws Status437UserNotFoundException;
}
