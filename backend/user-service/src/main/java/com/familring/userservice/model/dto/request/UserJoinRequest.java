package com.familring.userservice.model.dto.request;

import com.familring.userservice.model.dto.FamilyRole;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserJoinRequest {
    private String userKakaoId;
    private String userNickname;
    private Date userBirthDate;
    private FamilyRole userRole;
    private String userColor;
}
