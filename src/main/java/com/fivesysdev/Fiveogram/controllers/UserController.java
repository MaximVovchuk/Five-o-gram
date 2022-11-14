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
        return ResponseEntity.ok(userService.setAvatar(jwtUtil.getUsername(token), multipartFile));
    }

    @DeleteMapping("/avatar/{id}")
    public void deleteAvatar(@PathVariable long id,
                             @RequestHeader(value = "Authorization") String token)
            throws Status447NotYourAvatarException {
        userService.deleteAvatar(jwtUtil.validateTokenAndRetrieveUsername(token), id);
    }

    @PatchMapping("/editMyProfile")
    public ResponseEntity<User> editProfile(@ModelAttribute UserDTO userDTO,
                                            @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(userService.editMe(jwtUtil.getUsername(token), userDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable long id)
            throws Status437UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping("{id}/subscribe")
    public ResponseEntity<User> subscribe(@PathVariable long id,
                                          @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return ResponseEntity.ok(subscriptionService.subscribe(jwtUtil.getUsername(token), id));
    }

    @PostMapping("{id}/unsubscribe")
    public ResponseEntity<User> unsubscribe(@PathVariable long id,
                                            @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return ResponseEntity.ok(subscriptionService.unsubscribe(jwtUtil.getUsername(token), id));
    }

    @GetMapping("{id}/getUserSubscriptions")
    public ResponseEntity<List<User>> getUserSubscriptions(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserSubscriptions(id));
    }

    @GetMapping("{id}/getUserSubs")
    public ResponseEntity<List<User>> getUserSubs(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserSubs(id));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status437UserNotFoundException {
        return ResponseEntity.ok(notificationService.getAllNotifications(jwtUtil.getUsername(token)));
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<List<Post>> getRecommendations(@RequestHeader(value = "Authorization") String token)
            throws Status442NoRecommendationPostsException {
        return ResponseEntity.ok(userService.getRecommendations(jwtUtil.getUsername(token)));
    }

    @GetMapping("/getPostsWhereImMarked")
    public ResponseEntity<Set<Post>> getPostsWhereImMarked(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(userService.getPostsWhereImMarked(jwtUtil.getUsername(token)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String startsWith) {
        return ResponseEntity.ok(userService.searchByUsernameStartsWith(startsWith));
    }
}
