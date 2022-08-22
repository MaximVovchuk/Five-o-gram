package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.facadeInterfaces.FriendshipFacade;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final FriendshipFacade friendshipFacade;
    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(FriendshipFacade friendshipFacade, UserService userService, NotificationService notificationService) {
        this.friendshipFacade = friendshipFacade;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/setAvatar")
    public Map<String, String> setAvatar(@RequestBody MultipartFile multipartFile) {
        return userService.setAvatar(multipartFile);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PostMapping("{id}/makeFriend")
    public Map<String, String> makeFriend(@PathVariable long id) {
        return friendshipFacade.addToFriends(id);
    }

    @PostMapping("{id}/unmakeFriend")
    public Map<String, String> unmakeFriend(@PathVariable long id) {
        return userService.unmakeFriend(id);
    }

    @GetMapping("/notifications")
    public List<String> getNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/getRecommendations")
    public List<Post> getRecommendations() {
        return userService.getRecommendations();
    }
}
