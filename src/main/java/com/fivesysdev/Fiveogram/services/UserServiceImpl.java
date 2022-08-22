package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.FriendshipRepository;
import com.fivesysdev.Fiveogram.repositories.PictureRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final PostService postService;
    private final FriendshipRepository friendshipRepository;

    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository, PostService postService, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.postService = postService;
        this.friendshipRepository = friendshipRepository;
    }

    public User getUser(long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Map<String, String> unmakeFriend(long id) {
        Friendship friendship = new Friendship();
        User friend = userRepository.findUserById(id);
        if (friend == null) {
            return Map.of("Message", "friend not found");
        }
        friendship.setFriend(friend);
        friendship.setOwner(Context.getUserFromContext());
        if (friendshipRepository.findFriendshipByFriendAndOwner(friend, Context.getUserFromContext()) == null) {
            return Map.of("Message", "You are not friends");
        }
        try {
            friendshipRepository.delete(friendship);
        } catch (Exception ex) {
            return Map.of("Message", ex.getMessage());
        }
        return Map.of("Message", "ok");
    }

    @Override
    public Map<String, String> setAvatar(MultipartFile multipartFile) {
        User temp = Context.getUserFromContext();
        User user = userRepository.findUserById(temp.getId());
        try {
            Picture picture = new Picture(multipartFile.getBytes());
            picture.setCreated(LocalDate.now());
            pictureRepository.save(picture);
            user.setAvatar(picture);
        } catch (IOException e) {
            return Map.of("Message", "Error");
        }
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
