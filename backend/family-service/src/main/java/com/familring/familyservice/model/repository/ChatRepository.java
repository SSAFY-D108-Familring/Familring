package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.time.LocalDateTime;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Page<Chat> findByFamilyIdAndCreatedAtAfter(String familyId, LocalDateTime createdAt, Pageable pageable);
}
