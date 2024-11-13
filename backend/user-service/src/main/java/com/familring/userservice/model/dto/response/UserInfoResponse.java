package com.familring.userservice.model.dto.response;

import com.familring.userservice.model.dto.FamilyRole;
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
public class UserInfoResponse {
    private Long userId;
    private String userKakaoId;
    private String userNickname;
    private boolean userIsLunar;
    private LocalDate userBirthDate;
    private String userZodiacSign;
    private FamilyRole userRole;
    private String userFace;
    private String userColor;
    private String userEmotion;
    private String userFcmToken;
    private Integer userUnReadCount;
}
