package com.familring.userservice.model.dto.request;

import com.familring.userservice.model.dto.FamilyRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDeleteRequest {
    private String beforeUserKakaoId;
    private String afterUserKakaoId;
    private String userPassword;
    private String userName;
    private FamilyRole userRole;
    private String userFace;
    private String userEmotion;
    private String userFcmToken;
    private boolean userIsDeleted;
}
