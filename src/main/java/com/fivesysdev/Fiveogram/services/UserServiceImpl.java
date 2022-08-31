package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Friendship;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ResponseEntity<User> findUserById(long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Map<String, String>> setAvatar(MultipartFile multipartFile) {
        User user = userRepository.findUserById(Context.getUserFromContext().getId());
        if(multipartFile==null){
            return new ResponseEntity<>(Map.of("Message","didn`t receive picture"),HttpStatus.BAD_REQUEST);
        }
        Picture picture;
        try {
            picture = fileService.saveFile(multipartFile);
        }
        catch (FileException ex){
            return new ResponseEntity<>(Map.of("Message","File Exception"),HttpStatus.BAD_REQUEST);
        }
        user.setAvatar(picture);
        return new ResponseEntity<>(Map.of("Message", "ok"), HttpStatus.OK);
    }

    @Override
    public List<User> getFriendsList() {
        User user = userRepository.findUserById(Context.getUserFromContext().getId());
        List<User> result = new ArrayList<>();
        for (Friendship friendship : user.getFriendships()) {
            result.add(friendship.getFriend());
        }
        return result;
    }

    @Override
    public ResponseEntity<List<Post>> getRecommendations() {
        List<Post> posts = getFriendsList().stream().flatMap
                (friend -> postService.findAll(friend).stream().limit(5)).toList();
        if (!posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
