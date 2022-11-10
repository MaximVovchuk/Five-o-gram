package com.fivesysdev.Fiveogram.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status442NoRecommendationPostsException;
import com.fivesysdev.Fiveogram.models.Avatar;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.AvatarRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final FileService fileService;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PostService postService,
                           FileService fileService, AvatarRepository avatarRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.fileService = fileService;
        this.avatarRepository = avatarRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserById(long id) throws Status437UserNotFoundException {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new Status437UserNotFoundException();
        }
        return user;
    }


    @Override
    public User setAvatar(String username, MultipartFile multipartFile) throws Status441FileIsNullException {
        if (multipartFile == null) {
            throw new Status441FileIsNullException();
        }
        User user = userRepository.findUserByUsername(username);
        String uri = fileService.saveFile(user,multipartFile);
        Avatar avatar = new Avatar();
        avatar.setPath(uri);
        avatar.setUser(user);
        avatarRepository.save(avatar);
        user.addAvatar(avatar);
        return user;
    }

    @Override
    public List<User> getFriendsList(String username) {
        User user = userRepository.findUserByUsername(username);
        List<User> result = new ArrayList<>();
        for (Subscription subscription : user.getSubscriptions()) {
            result.add(subscription.getFriend());
        }
        return result;
    }

    @Override
    public User editMe(String username, UserDTO userDTO) {
        User user = userRepository.findUserByUsername(username);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setSurname(userDTO.getSurname());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public List<User> searchByUsernameStartsWith(String startsWith) {
        return userRepository.findByUsernameStartsWith(startsWith);
    }

    @Override
    public List<Post> getRecommendations(String username) throws Status442NoRecommendationPostsException {
        List<Post> posts = getFriendsList(username).stream().flatMap
                (friend -> postService.findAll(friend).stream().limit(5)).toList();
        if (!posts.isEmpty()) {
            return posts;
        }
        throw new Status442NoRecommendationPostsException();
    }
}
