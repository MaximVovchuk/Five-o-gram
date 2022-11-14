package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.PostResponseDTO;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status442NoRecommendationPostsException;
import com.fivesysdev.Fiveogram.exceptions.Status447NotYourAvatarException;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.AvatarRepository;
import com.fivesysdev.Fiveogram.repositories.MarkRepository;
import com.fivesysdev.Fiveogram.repositories.SubscriptionRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final FileService fileService;
    private final SubscriptionRepository subscriptionRepository;
    private final AvatarRepository avatarRepository;
    private final MarkRepository markRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PostService postService,
                           FileService fileService,
                           SubscriptionRepository subscriptionRepository,
                           AvatarRepository avatarRepository,
                           MarkRepository markRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.fileService = fileService;
        this.subscriptionRepository = subscriptionRepository;
        this.avatarRepository = avatarRepository;
        this.markRepository = markRepository;
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
        String uri = fileService.saveFile(user, multipartFile);
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
        return getUserSubscriptions(user.getId());
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
    public Set<Post> getPostsWhereImMarked(String username) {
        Set<Post> posts = new HashSet<>();
        for (Mark mark : markRepository.findByUsername(username)) {
            posts.add(mark.getPicture().getPost());
        }
        return posts;
    }

    @Override
    public void deleteAvatar(String username, long id) throws Status447NotYourAvatarException {
        Avatar avatar = avatarRepository.getById(id);
        if (avatar.getUser().getUsername().equals(username)) {
            avatarRepository.deleteById(id);
        } else throw new Status447NotYourAvatarException();
    }

    public List<User> getUserSubscriptions(long id) {
        return subscriptionRepository.findAllByOwner_Id(id).stream().map(Subscription::getFriend).collect(Collectors.toList());
    }

    public List<User> getUserSubs(long id) {
        return subscriptionRepository.findAllByFriend_id(id).stream().map(Subscription::getOwner).collect(Collectors.toList());
    }

    @Override
    public List<Post> getRecommendations(String username) throws Status442NoRecommendationPostsException {
        List<Post> posts = new java.util.ArrayList<>(getFriendsList(username).stream().flatMap
                (friend -> postService.findAll(friend).stream().map(PostResponseDTO::getPost).limit(5)).toList());
        if (posts.isEmpty()) {
            throw new Status442NoRecommendationPostsException();
        }
        posts.sort(Comparator.comparing(Post::getPubDate));
        return posts;
    }
}
