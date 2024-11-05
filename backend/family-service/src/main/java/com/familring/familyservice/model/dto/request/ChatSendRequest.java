package com.familring.familyservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChatSendRequest {
    @Schema(description = "가족 id", example = "1")
    private String familyId; // 가족 ID (가족 단위로 채팅이 구성됨)
    @Schema(description = "회원 id, 발신자", example = "1")
    private String userId;   // 메시지를 보낸 사용자 ID
    @Schema(description = "채팅 내용", example = "안녕하세요.")
    private String comment;  // 채팅 내용
    @Schema(description = "투표 여부(true면 투표 메시지, false면 일반 메시지)", example = "false")
    private boolean isVote; // 투표 여부 (true면 투표 메시지, false면 일반 메시지)
    @Schema(description = "투표 제목(isVote가 true일 때만 사용)", example = "오늘 저녁 치킨 어때요?")
    private String voteTitle; // 투표 제목 (isVote가 true일 때만 사용)
}
