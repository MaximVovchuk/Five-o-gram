package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status431FriendshipException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<?> setAvatar(@RequestBody MultipartFile multipartFile) throws Status441FileException {
        return userService.setAvatar(multipartFile);
    }

    @PatchMapping("/editMyProfile")
    public ResponseEntity<?> editProfile(@ModelAttribute UserDTO userDTO) {
        return userService.editMe(userDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) throws Status437UserNotFoundException {
        return userService.findUserById(id);
    }

    @PostMapping("{id}/makeFriend")
    public ResponseEntity<?> makeFriend(@PathVariable long id) throws Status437UserNotFoundException, Status431FriendshipException {
        return friendshipService.addToFriends(id);
    }

    @PostMapping("{id}/unmakeFriend")
    public ResponseEntity<?> unmakeFriend(@PathVariable long id) throws Status437UserNotFoundException, Status431FriendshipException {
        return friendshipService.unmakeFriend(id);
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
