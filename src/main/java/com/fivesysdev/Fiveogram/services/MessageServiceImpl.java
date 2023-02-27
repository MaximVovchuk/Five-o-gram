package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.MessageModel;
import com.fivesysdev.Fiveogram.repositories.MessageRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public void save(MessageModel model) {
        messageRepository.save(model);
    }
}
