package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.FriendshipService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> setAvatar(@RequestBody MultipartFile multipartFile) {
        try {
            return userService.setAvatar(multipartFile);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/editMyProfile")
    public ResponseEntity<?> editProfile(@ModelAttribute UserDTO userDTO){
        try {
            return userService.editMe(userDTO);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        try {
            return userService.findUserById(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{id}/makeFriend")
    public ResponseEntity<?> makeFriend(@PathVariable long id) {
        try {
            return friendshipService.addToFriends(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{id}/unmakeFriend")
    public ResponseEntity<?> unmakeFriend(@PathVariable long id) {
        try {
            return friendshipService.unmakeFriend(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
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
