package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.exceptions.UserNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(FriendshipService friendshipService, UserService userService, NotificationService notificationService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/setAvatar")
    public ResponseEntity<Map<String, String>> setAvatar(@RequestBody MultipartFile multipartFile) {
        return userService.setAvatar(multipartFile);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        try {
            return userService.findUserById(id);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{id}/makeFriend")
    public ResponseEntity<Map<String, String>> makeFriend(@PathVariable long id) {
        try {
            return friendshipService.addToFriends(id);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(Map.of("Message", "User was not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{id}/unmakeFriend")
    public ResponseEntity<Map<String, String>> unmakeFriend(@PathVariable long id) {
        try {
            return friendshipService.unmakeFriend(id);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(Map.of("Message", "User was not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<String>> getNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<List<Post>> getRecommendations() {
        return userService.getRecommendations();
    }
}
