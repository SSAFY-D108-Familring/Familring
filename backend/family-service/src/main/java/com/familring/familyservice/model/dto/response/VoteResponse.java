package com.familring.familyservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    // 채팅방 정보
    private ObjectId chatId;
    private Long roomId;

    // 응답자 정보
    private Long senderId; // 투표 응답자 id
    private UserInfoResponse sender; // 투표 응답자 정보
    private String voteResponse; // 투표 응답자의 응답

    // 투표 정보
    private String voteTitle; // 투표 제목
    private Long voteMakerId; // 투표 만든사람 Id
    private Map<Long, String> choices; // 사용자 ID와 그들의 선택 옵션

}
