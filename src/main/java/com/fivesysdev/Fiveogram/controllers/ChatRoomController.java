package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status452ChatRoomNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status453NotYourChatRoomException;
import com.fivesysdev.Fiveogram.exceptions.Status454YouAreNotAnAdminException;
import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.MessageModel;
import com.fivesysdev.Fiveogram.serviceInterfaces.ChatRoomService;
import com.fivesysdev.Fiveogram.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatRoom")
@Api(value = "Chatroom endpoints", tags = {"ChatRoom"})
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JWTUtil jwtUtil;

    public ChatRoomController(ChatRoomService chatRoomService, JWTUtil jwtUtil) {
        this.chatRoomService = chatRoomService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/new")
    public Response<ChatRoom> newChatRoom(@ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token,
                                          @RequestBody List<Long> usersId) throws Status437UserNotFoundException {
        return new Response<>(chatRoomService.newChatRoom(jwtUtil.getUsername(token), usersId));
    }

    @GetMapping("/{id}")
    public Response<List<MessageModel>> getMessages(@ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token,
                                                    @PathVariable long id)
            throws Status452ChatRoomNotFoundException, Status453NotYourChatRoomException {
        return new Response<>(chatRoomService.findById(id,jwtUtil.getUsername(token)).getMessages());
    }

    @PostMapping("/addUser/{chatRoomId}")
    public void addUserToChatRoom(@ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token,
                                  @PathVariable Long chatRoomId, @RequestBody List<Long> userIds)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        chatRoomService.addUserToChatRoom(chatRoomId, userIds, jwtUtil.getUsername(token));
    }

    @DeleteMapping("/deleteUser/{chatRoomId}")
    public void deleteUserFromChatRoom(@ApiParam(hidden = true) @RequestHeader(value = "Authorization") String token,
                                       @PathVariable Long chatRoomId, @RequestParam Long userId)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        chatRoomService.deleteUserFromChatRoom(chatRoomId, userId,jwtUtil.getUsername(token));
    }
}
