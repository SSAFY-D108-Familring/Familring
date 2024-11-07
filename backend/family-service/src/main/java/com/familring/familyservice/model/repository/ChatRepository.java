package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, ObjectId> {
    List<Chat> findAllByRoomId(Long roomId);

    Chat findByRoomId(Long roomId);
}

