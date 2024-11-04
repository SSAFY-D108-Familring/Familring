package com.familring.familyservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VoteParticipationRequest {
    @Schema(description = "투표 고유번호", example = "123456798")
    private String voteId;
    @Schema(description = "회원 고유번호", example = "1")
    private Long userId;
    @Schema(description = "투표 내용", example = "찬성")
    private String voteComment; 
}
