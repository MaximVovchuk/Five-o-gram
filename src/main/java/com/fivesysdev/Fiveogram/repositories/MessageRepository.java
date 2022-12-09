package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.ChatRoom;
import com.fivesysdev.Fiveogram.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel,Long> {
    List<MessageModel> findAllByChatRoom(ChatRoom chatRoom);
}
