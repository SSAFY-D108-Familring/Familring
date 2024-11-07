package com.familring.userservice.service.jwt;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.user.InvalidCredentialsException;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtTokenServiceImpl implements JwtTokenService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public JwtTokenResponse generateToken(String userName, String rawPassword) {
        // 0. 응답 객체 생성
        Authentication authentication;

        // 1. userProviderId를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, rawPassword);
        log.info("UsernamePasswordAuthenticationToken: {}", authenticationToken);

        try {
            // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
            authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Authentication: {}", authentication);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new InvalidCredentialsException();
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenResponse jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. 리프레시 토큰 저장 - Redis
        redisService.saveRefreshToken(userName, jwtToken.getRefreshToken());

        return jwtToken;
    }

    @Override
    public String getRefreshToken(String userName) {
        // Redis 사용
        return redisService.getRefreshToken(userName);
    }
}
