package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.MessageModel;
import com.fivesysdev.Fiveogram.repositories.MessageRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void save(MessageModel model) {
        messageRepository.save(model);
    }

    @Override
    public List<MessageModel> getMessagesByChatRoom(ChatRoom chatRoom) {
        return messageRepository.findAllByChatRoom(chatRoom);
    }
}
