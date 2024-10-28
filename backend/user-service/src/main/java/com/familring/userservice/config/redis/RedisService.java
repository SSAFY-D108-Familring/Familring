package com.familring.userservice.config.redis;

public interface RedisService {
    // refreshToken 저장
    void saveRefreshToken(String userName, String refreshToken);
    
    // refreshToken 조회
    String getRefreshToken(String userName);
    
    // refreshToken 삭제
    void deleteRefreshToken(String userName);
}
