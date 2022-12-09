package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
