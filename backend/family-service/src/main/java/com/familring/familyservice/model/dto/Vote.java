package com.familring.familyservice.model.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "votes")
@ToString
public class Vote {
    @Id
    private ObjectId voteId;

    // 투표 정보
    private String voteTitle; // 투표 제목
    private Long voteMakerId; // 투표 만든사람 Id
    private Integer familyCount; // 가족 구성원 수
    private boolean isCompleted; // 투표 완료 여부
    private LocalDateTime createdAt; // 투표 생성 시간
    private Map<String, Integer> voteResult; // 투표 결과 (예: {"찬성": 3, "반대": 2})
    private Map<Long, String> choices; // 사용자 ID와 그들의 선택 옵션

    // 채팅 정보
    private Long roomId; // 채팅 방의 id == familyId
    private Long senderId; // 발신자 id
}
