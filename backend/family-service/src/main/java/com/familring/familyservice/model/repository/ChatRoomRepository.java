package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
