package com.fivesysdev.Fiveogram.ws;

import com.fivesysdev.Fiveogram.exceptions.Status452ChatRoomNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status453NotYourChatRoomException;
import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.MessageModel;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.serviceInterfaces.ChatRoomService;
import com.fivesysdev.Fiveogram.serviceInterfaces.MessageService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@ServerEndpoint(value = "/chat/{chatRoomId}", decoders = MessageModelDecoder.class, encoders = MessageModelEncoder.class)
public class ChatWebsocketEndpoint {

    private static final Set<ChatRoom> chatRooms = Collections.synchronizedSet(new HashSet<>());
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final MessageService messageService;

    public ChatWebsocketEndpoint() {
        this.chatRoomService = SpringContext.getApplicationContext().getBean(ChatRoomService.class);
        this.userService = SpringContext.getApplicationContext().getBean(UserService.class);
        this.messageService = SpringContext.getApplicationContext().getBean(MessageService.class);
    }

    @OnMessage
    public String onMessage(Session session, String message, @PathParam("chatRoomId") long chatRoomId) throws IOException {
        System.out.println("Handling message: " + message);
        User user = userService.findUserByUsername(session.getUserPrincipal().getName());
        ChatRoom chatRoom = chatRooms.stream().filter
                (chatRoom1 -> chatRoom1.getId() == chatRoomId).findAny().orElseThrow();
        MessageModel model = new MessageModel(message, user, chatRoom);
        messageService.save(model);
        for (Session s : chatRoom.getSessions()) {
            s.getBasicRemote().sendText(message);
        }
        return null;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatRoomId") long chatRoomId)
            throws Status453NotYourChatRoomException, Status452ChatRoomNotFoundException {
        System.out.println("On open: " + session.getId());
        ChatRoom chatRoom = chatRooms.stream().filter
                        (chatRoom1 -> chatRoom1.getId() == chatRoomId).findAny()
                .orElse(chatRoomService.findById(chatRoomId, session.getUserPrincipal().getName()));
        chatRoom.addSession(session);
        chatRooms.add(chatRoom);
    }

    @OnClose
    public void onClose(Session session, @PathParam("chatRoomId") long chatRoomId) {
        System.out.println("On close: " + session.getId());
        ChatRoom chatRoom = chatRooms.stream().filter
                (chatRoom1 -> chatRoom1.getId() == chatRoomId).findAny().orElse(new ChatRoom());
        chatRoom.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(throwable.toString());
    }

}
