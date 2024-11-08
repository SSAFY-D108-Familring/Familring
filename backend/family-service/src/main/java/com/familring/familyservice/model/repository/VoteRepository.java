package com.familring.familyservice.model.repository;

import com.familring.familyservice.model.dto.Vote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoteRepository extends MongoRepository<Vote, String> {
    Optional<Vote> findByVoteId(String voteId);
}
