package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUser(long id);

    Map<String, String> unmakeFriend(long id);

    Map<String, String> setAvatar(MultipartFile multipartFile);

    List<Post> getRecommendations();

    List<User> getFriendsList();
}
