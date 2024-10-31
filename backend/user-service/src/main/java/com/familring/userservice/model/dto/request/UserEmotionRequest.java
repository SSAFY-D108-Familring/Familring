package com.familring.userservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserEmotionRequest {
    @Schema(description = "회원 기분", example = "기뻐요 😄")
    private String userEmotion;
}
