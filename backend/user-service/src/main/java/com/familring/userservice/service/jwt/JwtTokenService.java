package com.familring.userservice.service.jwt;

import com.familring.userservice.model.dto.response.JwtTokenResponse;

public interface JwtTokenService {
    // AccessToken, RefreshToken 발급
    JwtTokenResponse generateToken(String userName, String rawPassword);

    // refreshToken 조회
    String getRefreshToken(String userName);
}
