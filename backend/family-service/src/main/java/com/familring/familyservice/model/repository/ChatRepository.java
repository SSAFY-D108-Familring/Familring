package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByRoomId(Long roomId);
    Page<Chat> findByRoomId(Long roomId, Pageable pageable);
}

