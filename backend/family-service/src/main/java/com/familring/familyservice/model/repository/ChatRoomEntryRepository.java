package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.ChatRoomEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface ChatRoomEntryRepository extends MongoRepository<ChatRoomEntry, String> {
    Optional<ChatRoomEntry> findByFamilyIdAndUserId(String familyId, String userId);
}

