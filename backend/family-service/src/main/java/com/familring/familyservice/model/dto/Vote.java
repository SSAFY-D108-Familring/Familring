package com.familring.familyservice.model.dto;

import com.familring.familyservice.exception.chat.AlreadyParticipatedException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Document(collection = "family_vote")
public class Vote {

    @Id
    private String id; // MongoDB의 Document ID

    private String voteTitle; // 투표 제목
    private Map<String, String> participantsChoices; // 사용자 ID와 그들의 선택 옵션
    private boolean isCompleted; // 투표 완료 여부
    private LocalDateTime createdAt; // 투표 생성 시간
    private Map<String, Integer> voteResult; // 투표 결과 (예: {"찬성": 3, "반대": 2})

    // 참여자 추가 및 선택 옵션 업데이트
    public void addParticipant(String userId, String option) {
        // 사용자가 이미 참여한 경우 예외 발생
        if (participantsChoices.containsKey(userId)) {
            throw new AlreadyParticipatedException();
        }
        // 참여자 목록에 추가하고 결과 업데이트
        participantsChoices.put(userId, option);
        voteResult.put(option, voteResult.getOrDefault(option, 0) + 1);
    }

    // 모든 가족 구성원이 참여했는지 확인
    public boolean checkIfCompleted(int familySize) {
        this.isCompleted = participantsChoices.size() >= familySize;
        return this.isCompleted;
    }
}
