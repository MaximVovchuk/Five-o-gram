package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.MessageModel;

import java.util.List;

public interface MessageService {
    void save(MessageModel model);
}
