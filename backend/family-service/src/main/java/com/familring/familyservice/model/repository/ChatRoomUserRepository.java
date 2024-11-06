package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
