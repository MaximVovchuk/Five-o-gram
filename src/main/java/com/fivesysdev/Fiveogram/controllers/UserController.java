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
import com.fivesysdev.Fiveogram.util.Response;
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
    public Response<User> setAvatar(@RequestBody MultipartFile multipartFile,
                                          @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException {
        return new Response<>(userService.setAvatar(jwtUtil.getUsername(token), multipartFile));
    }

    @DeleteMapping("/avatar/{id}")
    public void deleteAvatar(@PathVariable long id,
                             @RequestHeader(value = "Authorization") String token)
            throws Status447NotYourAvatarException, Status450AvatarNotFoundException {
        userService.deleteAvatar(jwtUtil.getUsername(token), id);
    }

    @PatchMapping("/editMyProfile")
    public Response<String> editProfile(@RequestBody UserDTO userDTO,
                                            @RequestHeader(value = "Authorization") String token)
            throws Status439UsernameBusyException {
        return new Response<>(userService.editMe(jwtUtil.getUsername(token), userDTO));
    }

    @GetMapping("{id}")
    public Response<User> getUser(@PathVariable long id)
            throws Status437UserNotFoundException {
        return new Response<>(userService.findUserById(id));
    }

    @PostMapping("{id}/subscribe")
    public Response<User> subscribe(@PathVariable long id,
                                          @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return new Response<>(subscriptionService.subscribe(jwtUtil.getUsername(token), id));
    }

    @PostMapping("{id}/unsubscribe")
    public Response<User> unsubscribe(@PathVariable long id,
                                            @RequestHeader(value = "Authorization") String token)
            throws Status437UserNotFoundException, Status431SubscriptionException {
        return new Response<>(subscriptionService.unsubscribe(jwtUtil.getUsername(token), id));
    }

    @GetMapping("{id}/getUserSubscriptions")
    public Response<List<User>> getUserSubscriptions(@PathVariable long id) throws Status437UserNotFoundException {
        return new Response<>(userService.getUserSubscriptions(id));
    }

    @GetMapping("{id}/getUserSubs")
    public Response<List<User>> getUserSubs(@PathVariable long id) throws Status437UserNotFoundException {
        return new Response<>(userService.getUserSubs(id));
    }

    @GetMapping("/notifications")
    public Response<List<Notification>> getNotifications(@RequestHeader(value = "Authorization") String token)
            throws Status435PostNotFoundException, Status437UserNotFoundException {
        return new Response<>(notificationService.getAllNotifications(jwtUtil.getUsername(token)));
    }

    @GetMapping("/getFeed")
    public Response<List<Post>> getFeed(@RequestHeader(value = "Authorization") String token)
            throws Status442NoFeedPostsException, Status437UserNotFoundException {
        return new Response<>(userService.getFeed(jwtUtil.getUsername(token)));
    }

    @GetMapping("/getPostsWhereImMarked")
    public Response<Set<Post>> getPostsWhereImMarked(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(userService.getPostsWhereImMarked(jwtUtil.getUsername(token)));
    }

    @GetMapping("/search")
    public Response<?> search(@RequestParam String startsWith) {
        return new Response<>(userService.searchByUsernameStartsWith(startsWith));
    }
    @DeleteMapping("/delete")
    public void deleteMe(@RequestHeader(value = "Authorization") String token){
        userService.deleteMe(jwtUtil.getUsername(token));
    }
}
