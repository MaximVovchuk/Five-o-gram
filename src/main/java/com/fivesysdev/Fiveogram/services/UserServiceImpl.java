package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final FileService fileService;

    public UserServiceImpl(UserRepository userRepository, PostService postService,
                           FileService fileService) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.fileService = fileService;
    }

    public User findUserById(long id) {
        return userRepository.findUserById(id);
    }



    @Override
    public Map<String, String> setAvatar(MultipartFile multipartFile) {
        User temp = Context.getUserFromContext();
        User user = userRepository.findUserById(temp.getId());
        Picture picture = fileService.saveFile(multipartFile);
        user.setAvatar(picture);
        return Map.of("Message", "ok");
    }

    @Override
    public List<User> getFriendsList() {
        User temp = Context.getUserFromContext();
        User user = userRepository.findUserById(temp.getId());
        List<User> result = new ArrayList<>();
        for (Friendship friendship : user.getFriendships()) {
            result.add(friendship.getFriend());
        }
        return result;
    }

    @Override
    public List<Post> getRecommendations() {
        return getFriendsList().stream().flatMap
                (friend -> postService.findAll(friend).stream().limit(5)).collect(Collectors.toList());
    }
}
