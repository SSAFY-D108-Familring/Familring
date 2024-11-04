package com.familring.timecapsuleservice.repository;

import com.familring.timecapsuleservice.domain.TimeCapsule;
import com.familring.timecapsuleservice.domain.TimeCapsuleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TimeCapsuleAnswerRepository extends JpaRepository<TimeCapsuleAnswer, Integer> {
    // 해당 user 가 작성한 타임 캡슐 답변 찾기
    Optional<TimeCapsuleAnswer> getTimeCapsuleAnswerByUserIdAndTimecapsule(Long userId, TimeCapsule timeCapsule);

    // 타임 캡슐 id 로 답변 찾기
    List<TimeCapsuleAnswer> getTimeCapsuleAnswerByTimecapsule(TimeCapsule timeCapsule);

    // 타임 캡슐, 타임 캡슐 답변으로 UserId 찾기
    Long getTimeCapsuleAnswerByIdAndTimecapsule(Long id, TimeCapsule timeCapsule);
}
