package com.familring.userservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginRequest {
    @Schema(description = "Kakao에서 제공하는 Id", example = "123456789")
    private String userKakaoId;
}
