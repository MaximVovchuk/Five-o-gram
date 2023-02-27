package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.AvatarRepository;
import com.fivesysdev.Fiveogram.repositories.MarkRepository;
import com.fivesysdev.Fiveogram.repositories.SubscriptionRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final FileService fileService;
    private final SubscriptionRepository subscriptionRepository;
    private final AvatarRepository avatarRepository;
    private final MarkRepository markRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(Long id) throws Status437UserNotFoundException {
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
    public List<User> getFriendsList(String username) throws Status437UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        return getUserSubscriptions(user.getId());
    }

    @Override
    public String editMe(String username, UserDTO userDTO) throws Status439UsernameBusyException {
        User user = userRepository.findUserByUsername(username);
        if (!username.equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Status439UsernameBusyException();
        }
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setSurname(userDTO.getSurname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return jwtUtil.generateToken(user.getUsername(), List.of(user.getRole()));
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
    public void deleteAvatar(String username, Long id)
            throws Status447NotYourAvatarException, Status450AvatarNotFoundException {
        if (!avatarRepository.existsById(id)) {
            throw new Status450AvatarNotFoundException();
        }
        Avatar avatar = avatarRepository.getById(id);
        if (avatar.getUser().getUsername().equals(username)) {
            avatarRepository.deleteById(id);
        } else throw new Status447NotYourAvatarException();
    }

    @Override
    public List<User> getUserSubscriptions(Long id) throws Status437UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new Status437UserNotFoundException();
        }
        return subscriptionRepository.findAllByOwner_Id(id).stream().map(Subscription::getFriend).collect(Collectors.toList());
    }

    @Override
    public List<User> getUserSubs(Long id) throws Status437UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new Status437UserNotFoundException();
        }
        return subscriptionRepository.findAllByFriend_id(id).stream().map(Subscription::getOwner).collect(Collectors.toList());
    }

    @Override
    public List<Post> getFeed(String username)
            throws Status442NoFeedPostsException, Status437UserNotFoundException {
        List<Post> posts = new ArrayList<>(getFriendsList(username).stream().flatMap
                (friend -> postService.findAll(friend).stream().limit(5)).toList());
        if (posts.isEmpty()) {
            throw new Status442NoFeedPostsException();
        }
        posts.sort(Comparator.comparing(Post::getPubDate));
        return posts;
    }
}
