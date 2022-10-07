package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.Status431SubscriptionException;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.serviceInterfaces.SubscriptionService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<?> setAvatar(@RequestBody MultipartFile multipartFile,
                                       @RequestHeader(value = "Authorization") String token) throws Status441FileException {
        return userService.setAvatar(jwtUtil.validate(token),multipartFile);
    }

    @PatchMapping("/editMyProfile")
    public ResponseEntity<?> editProfile(@ModelAttribute UserDTO userDTO,
                                         @RequestHeader(value = "Authorization") String token) {
        return userService.editMe(jwtUtil.validate(token),userDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) throws Status437UserNotFoundException {
        return userService.findUserById(id);
    }

    @PostMapping("{id}/makeFriend")
    public ResponseEntity<?> subscribe(@PathVariable long id,
                                       @RequestHeader(value = "Authorization") String token) throws Status437UserNotFoundException, Status431SubscriptionException {
        return subscriptionService.subscribe(jwtUtil.validate(token),id);
    }

    @PostMapping("{id}/unmakeFriend")
    public ResponseEntity<?> unsubscribe(@PathVariable long id,
                                         @RequestHeader(value = "Authorization") String token) throws Status437UserNotFoundException, Status431SubscriptionException {
        return subscriptionService.unsubscribe(jwtUtil.validate(token),id);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<String>> getNotifications(@RequestHeader(value = "Authorization") String token) {
        return notificationService.getAllNotifications(jwtUtil.validate(token));
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<List<Post>> getRecommendations(@RequestHeader(value = "Authorization") String token) {
        return userService.getRecommendations(jwtUtil.validate(token));
    }
}
