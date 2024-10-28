package com.familring.userservice.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    @Override
    public void saveRefreshToken(String userName, String refreshToken) {
        redisTemplate.opsForValue().set(
                "RefreshToken:" + userName,
                refreshToken,
                refreshTokenExpireTime,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public String getRefreshToken(String userName) {
        return redisTemplate.opsForValue().get("RefreshToken:" + userName);
    }

    @Override
    public void deleteRefreshToken(String userName) {
        redisTemplate.delete("RefreshToken:" + userName);
    }
}
