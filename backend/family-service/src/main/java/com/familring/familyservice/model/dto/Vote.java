package com.familring.familyservice.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(collection = "family_vote")
public class Vote {

    @Id
    private String id; // MongoDB의 Document ID

    private String voteTitle; // 투표 제목
    private List<String> participants; // 투표에 참여한 사용자 ID 목록
    private boolean isCompleted; // 투표 완료 여부
    private LocalDateTime createdAt; // 투표 생성 시간
    private Map<String, Integer> voteResult; // 투표 결과 (예: {"찬성": 3, "반대": 2})

    public void addParticipant(String userId) {
        // 사용자를 참여자 목록에 추가하고, 중복 추가 방지
        if (!participants.contains(userId)) {
            participants.add(userId);
        }
    }

    public boolean checkIfCompleted(int familySize) {
        // 모든 가족 구성원이 투표에 참여했는지 확인
        this.isCompleted = participants.size() >= familySize;
        return this.isCompleted;
    }

    public void updateVoteResult(String option) {
        // 옵션에 해당하는 투표 결과 업데이트
        voteResult.put(option, voteResult.getOrDefault(option, 0) + 1);
    }
}
