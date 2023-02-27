package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status452ChatRoomNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status453NotYourChatRoomException;
import com.fivesysdev.Fiveogram.exceptions.Status454YouAreNotAnAdminException;
import com.fivesysdev.Fiveogram.models.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    ChatRoom findById(Long id, String username) throws Status452ChatRoomNotFoundException, Status453NotYourChatRoomException;

    ChatRoom newChatRoom(String username, List<Long> usersId) throws Status437UserNotFoundException;

    void addUserToChatRoom(Long chatRoomId, List<Long> userIds, String username) throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException;

    void deleteUserFromChatRoom(Long chatRoomId, Long userId, String username) throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException;
}
