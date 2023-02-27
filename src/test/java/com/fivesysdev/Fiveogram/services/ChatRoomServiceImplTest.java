package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status437UserNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status452ChatRoomNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status453NotYourChatRoomException;
import com.fivesysdev.Fiveogram.exceptions.Status454YouAreNotAnAdminException;
import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.ChatRoomRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    private User admin;
    private User user1;
    private User user2;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        admin = User.builder().username("admin").password("password").build();
        admin.setId(1L);
        user1 = User.builder().username("user1").password("password").build();
        user1.setId(2L);
        user2 = User.builder().username("user2").password("password").build();
        user2.setId(3L);
        chatRoom = new ChatRoom(admin, new HashSet<>(Arrays.asList(user1, user2)));
        chatRoom.setId(1L);
    }

    @Test
    void testFindByIdWhenUserBelongsToChatRoom() throws Exception {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        ChatRoom result = chatRoomService.findById(1L, "user1");
        assertEquals(chatRoom, result);
    }

    @Test
    void testFindByIdWhenUserDoesNotBelongToChatRoom() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        Executable executable = () -> chatRoomService.findById(1L, "user3");
        assertThrows(Status453NotYourChatRoomException.class, executable);
    }

    @Test
    void testFindByIdWhenChatRoomNotFound() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.empty());
        Executable executable = () -> chatRoomService.findById(1L, "user1");
        assertThrows(Status452ChatRoomNotFoundException.class, executable);
    }

    @Test
    void testNewChatRoom() throws Exception {
        when(userService.findUserByUsername("admin")).thenReturn(admin);
        when(userService.findUserById(1L)).thenReturn(user1);
        when(userService.findUserById(2L)).thenReturn(user2);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
        List<Long> usersId = Arrays.asList(1L, 2L);
        ChatRoom result = chatRoomService.newChatRoom("admin", usersId);
        assertEquals(chatRoom, result);
    }

    @Test
    void testAddUserToChatroom() throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        User user3 = new User();
        when(userService.findUserById(4L)).thenReturn(user3);
        chatRoomService.addUserToChatRoom(1L, List.of(4L), "admin");
        assertTrue(chatRoom.getUsers().contains(user3));
    }

    @Test
    void testAddUserToChatroomThrows454() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        assertThrows(Status454YouAreNotAnAdminException.class,
                () -> chatRoomService.addUserToChatRoom(1L, List.of(4L), "user1"));
    }

    @Test
    void testDeleteUserFromChatroom() throws Status452ChatRoomNotFoundException, Status437UserNotFoundException, Status454YouAreNotAnAdminException {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        User user3 = new User();
        chatRoom.addUser(user3);
        when(userService.findUserById(4L)).thenReturn(user3);
        chatRoomService.deleteUserFromChatRoom(1L, 4L, "admin");
        assertFalse(chatRoom.getUsers().contains(user3));
    }

    @Test
    void testDeleteUserFromChatroomThrows454() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        assertThrows(Status454YouAreNotAnAdminException.class,
                () -> chatRoomService.deleteUserFromChatRoom(1L, 4L, "user1"));
    }
}