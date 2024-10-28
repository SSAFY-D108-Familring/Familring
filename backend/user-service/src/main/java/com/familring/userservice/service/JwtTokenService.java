package com.familring.userservice.service;

import com.familring.userservice.model.dto.response.JwtTokenResponse;
import org.springframework.security.core.Authentication;

public interface JwtTokenService {
    // AccessToken, RefreshToken 발급
    JwtTokenResponse generateToken(String userName, String rawPassword);

    // refreshToken 조회
    String getRefreshToken(String userName);
}
