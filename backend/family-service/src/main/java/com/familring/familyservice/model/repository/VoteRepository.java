package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.chat.Vote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface VoteRepository extends ReactiveMongoRepository<Vote, String> {
    Mono<Vote> findById(String id);
}
