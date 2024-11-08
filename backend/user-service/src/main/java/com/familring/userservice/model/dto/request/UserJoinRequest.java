package com.familring.userservice.model.dto.request;

import com.familring.userservice.model.dto.FamilyRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    @Schema(description = "Kakao에서 제공하는 Id", example = "123456789")
    private String userKakaoId;
    @Schema(description = "회원 별명", example = "test")
    private String userNickname;
    @Schema(description = "회원 생일 음력 여부", example = "false")
    private boolean userIsLunar;
    @Schema(description = "회원 생일", example = "2024-11-05")
    private LocalDate userBirthDate;
    @Schema(description = "회원 가족 역할(F: 아빠, M: 엄마, S: 아들, D: 딸)", example = "F")
    private FamilyRole userRole;
    @Schema(description = "회원 프로필 배경색", example = "#FFFFFF")
    private String userColor;
    @Schema(description = "회원 가족 생성 여부 확인", example = "true")
    private boolean isFirst;
    @Schema(description = "가족 코드(새로 생성인 경우 미첨부, 기존 참여인 경우 첨부)", example = "49EE36")
    private String familyCode;
}
