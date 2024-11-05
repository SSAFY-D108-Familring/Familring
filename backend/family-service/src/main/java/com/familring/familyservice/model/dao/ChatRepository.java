package com.familring.familyservice.model.dao;

import com.familring.familyservice.model.dto.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Page<Chat> findByFamilyId(String familyId, Pageable pageable);
}
