package com.familring.userservice.model.dto.request;

import com.familring.userservice.model.dto.FamilyRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserJoinRequest {
    @Schema(description = "Kakao에서 제공하는 Id", example = "123456789")
    private String userKakaoId;
    @Schema(description = "회원 별명", example = "test")
    private String userNickname;
    @Schema(description = "회원 별명", example = "2000-01-01")
    private Date userBirthDate;
    @Schema(description = "회원 가족 역할(F: 아빠, M: 엄마, S: 아들, D: 딸)", example = "F")
    private FamilyRole userRole;
    @Schema(description = "회원 프로필 배경색", example = "#FFFFFF")
    private String userColor;
}
