package com.familring.timecapsuleservice.dto.client;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserInfoResponse {
    private Long userId;
    private String userKakaoId;
    private String userNickname;
    private LocalDate userBirthDate;
    private String userZodiacSign;
    private FamilyRole userRole;
    private String userFace;
    private String userColor;
    private String userEmotion;
}
