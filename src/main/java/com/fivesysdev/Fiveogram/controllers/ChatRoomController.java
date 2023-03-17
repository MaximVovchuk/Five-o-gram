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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatRoom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JWTUtil jwtUtil;

    @PostMapping()
    public Response<ChatRoom> newChatRoom(@RequestHeader(value = "Authorization") String token,
                                          @RequestBody List<Long> usersId) throws Status437UserNotFoundException {
        return new Response<>(chatRoomService.newChatRoom(jwtUtil.getUsername(token), usersId));
    }

    @GetMapping("/{id}")
    public Response<List<MessageModel>> getMessages(@RequestHeader(value = "Authorization") String token,
                                                    @PathVariable Long id)
            throws Status452ChatRoomNotFoundException, Status453NotYourChatRoomException {
        return new Response<>(chatRoomService.findById(id,jwtUtil.getUsername(token)).getMessages());
    }

    @PostMapping("/{chatRoomId}/user")
    public void addUserToChatRoom(@RequestHeader(value = "Authorization") String token,
                                  @PathVariable Long chatRoomId, @RequestBody List<Long> userIds)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        chatRoomService.addUserToChatRoom(chatRoomId, userIds, jwtUtil.getUsername(token));
    }

    @DeleteMapping("/{chatRoomId}/user")
    public void deleteUserFromChatRoom(@RequestHeader(value = "Authorization") String token,
                                       @PathVariable Long chatRoomId, @RequestParam Long userId)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        chatRoomService.deleteUserFromChatRoom(chatRoomId, userId,jwtUtil.getUsername(token));
    }
}
