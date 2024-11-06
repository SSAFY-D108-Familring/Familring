package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatEntity, Long> {
    List<ChatEntity> findAllByRoomId(Long roomId);
}

