package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status452ChatRoomNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status453NotYourChatRoomException;
import com.fivesysdev.Fiveogram.exceptions.Status454YouAreNotAnAdminException;
import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.ChatRoomRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.ChatRoomService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Override
    public ChatRoom findById(Long id, String username) throws Status452ChatRoomNotFoundException, Status453NotYourChatRoomException {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(Status452ChatRoomNotFoundException::new);
        for (User user : chatRoom.getUsers()) {
            if (user.getUsername().equals(username)) {
                return chatRoom;
            }
        }
        throw new Status453NotYourChatRoomException();
    }

    @Override
    public ChatRoom newChatRoom(String username, List<Long> usersId) throws Status437UserNotFoundException {
        User admin = userService.findUserByUsername(username);
        Set<User> users = new HashSet<>();
        for (Long id : usersId) {
            users.add(userService.findUserById(id));
        }
        users.add(admin);
        return chatRoomRepository.save(new ChatRoom(admin, users));
    }

    @Override
    @Transactional
    public void addUserToChatRoom(Long chatRoomId, List<Long> userIds, String username)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException,
            Status454YouAreNotAnAdminException {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(Status452ChatRoomNotFoundException::new);
        if (chatRoom.getAdmin().getUsername().equals(username)) {
            for (Long userId : userIds) {
                chatRoom.addUser(userService.findUserById(userId));
            }
        } else {
            throw new Status454YouAreNotAnAdminException();
        }
    }

    @Override
    @Transactional
    public void deleteUserFromChatRoom(Long chatRoomId, Long userId, String username)
            throws Status452ChatRoomNotFoundException, Status437UserNotFoundException,
            Status454YouAreNotAnAdminException {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(Status452ChatRoomNotFoundException::new);
        if (chatRoom.getAdmin().getUsername().equals(username)) {
            chatRoom.removeUser(userService.findUserById(userId));
        } else {
            throw new Status454YouAreNotAnAdminException();
        }
    }
}
