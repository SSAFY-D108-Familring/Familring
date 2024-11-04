package com.familring.timecapsuleservice.repository;

import com.familring.timecapsuleservice.domain.TimeCapsule;
import com.familring.timecapsuleservice.domain.TimeCapsuleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeCapsuleAnswerRepository extends JpaRepository<TimeCapsuleAnswer, Integer> {
    Optional<TimeCapsuleAnswer> getTimeCapsuleAnswerByUserIdAndTimecapsule(Long userId, TimeCapsule timeCapsule);
}
