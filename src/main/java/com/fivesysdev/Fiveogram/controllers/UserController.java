package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.SubscriptionService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final JWTUtil jwtUtil;

    public UserController(SubscriptionService subscriptionService, UserService userService, NotificationService notificationService, JWTUtil jwtUtil) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/setAvatar")
    public ResponseEntity<User> setAvatar(@RequestBody MultipartFile multipartFile,
                                          @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException {
        return new ResponseEntity<>(
                userService.setAvatar(jwtUtil.validate(token), multipartFile),
                HttpStatus.OK);
    }

    @PatchMapping("/editMyProfile")
    public ResponseEntity<User> editProfile(@ModelAttribute UserDTO userDTO,
                                            @RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>(
                userService.editMe(jwtUtil.validate(token), userDTO),
                HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable long id)
            throws Status437UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PostMapping("{id}/subscribe")
    public ResponseEntity<User> subscribe(@PathVariable long id,
                                          @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return new ResponseEntity<>(
                subscriptionService.subscribe(jwtUtil.validate(token), id),
                HttpStatus.OK);
    }

    @PostMapping("{id}/unsubscribe")
    public ResponseEntity<User> unsubscribe(@PathVariable long id,
                                            @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return new ResponseEntity<>(
                subscriptionService.unsubscribe(jwtUtil.validate(token), id),
                HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status437UserNotFoundException {
        List<Notification> allNotifications = notificationService.getAllNotifications(jwtUtil.validate(token));
        return new ResponseEntity<>(
                allNotifications,
                HttpStatus.OK);
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<List<Post>> getRecommendations(@RequestHeader(value = "Authorization") String token)
            throws Status442NoRecommendationPostsException {
        return new ResponseEntity<>(
                userService.getRecommendations(jwtUtil.validate(token)),
                HttpStatus.OK);
    }

    @GetMapping("/getPostsWhereImMarked")
    public ResponseEntity<Set<Post>> getPostsWhereImMarked(@RequestHeader(value = "Authorization") String token) {
        Set<Post> postsWhereImMarked = userService.getPostsWhereImMarked(jwtUtil.validate(token));
        return new ResponseEntity<>(
                postsWhereImMarked,
                HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String startsWith) {
        return new ResponseEntity<>(userService.searchByUsernameStartsWith(startsWith), HttpStatus.OK);
    }
}
