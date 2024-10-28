package com.familring.userservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtTokenResponse {
    @Schema(description = "토큰 권한 타입", example = "Bearer")
    private String grantType;
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    @Schema(description = "JWT 리프레시 토큰", example = "dGhpcyBpcyByZWZyZXNoIHRva2Vu")
    private String refreshToken;
}
