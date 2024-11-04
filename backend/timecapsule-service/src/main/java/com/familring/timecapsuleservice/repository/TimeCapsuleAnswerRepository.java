package com.familring.timecapsuleservice.repository;

import com.familring.timecapsuleservice.domain.TimecapsuleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimecapsuleAnwerRepository extends JpaRepository<TimecapsuleAnswer, Integer> {
    Optional<TimecapsuleAnswer> findTimecapsuleAnswerByFamilyIdAndTimeCapsuleId(Integer familyId, Integer timeCapsuleId);
}
