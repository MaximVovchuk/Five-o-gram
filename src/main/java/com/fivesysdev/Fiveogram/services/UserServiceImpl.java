package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Avatar;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.AvatarRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<User> findUserById(long id) throws Status437UserNotFoundException {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new Status437UserNotFoundException();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<User> setAvatar(String username, MultipartFile multipartFile) throws Status441FileException {
        if (multipartFile == null) {
            throw new Status441FileException();
        }
        User user = userRepository.findUserByUsername(username);
        String uri = fileService.saveFile(user,multipartFile);
        Avatar avatar = new Avatar();
        avatar.setPath(uri);
        avatar.setUser(user);
        avatarRepository.save(avatar);
        user.addAvatar(avatar);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    public ResponseEntity<?> editMe(String username, UserDTO userDTO) {
        User user = userRepository.findUserByUsername(username);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setSurname(userDTO.getSurname());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public ResponseEntity<List<Post>> getRecommendations(String username) {
        List<Post> posts = getFriendsList(username).stream().flatMap
                (friend -> postService.findAll(friend).stream().limit(5)).toList();
        if (!posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
