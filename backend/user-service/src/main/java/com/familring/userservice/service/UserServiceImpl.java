package com.familring.userservice.service;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.InvalidTokenException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;

    @Override
    @Transactional
    public JwtTokenResponse login(UserLoginRequest userLogInRequest) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findByUserKakaoId(userLogInRequest.getUserKakaoId())
                // 2-1. 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userLogInRequest.getUserKakaoId()));

        // 2-2. 회원이 있는 경우 -> JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(user.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    @Transactional
    public JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image){
        // 1. 사용자 회원가입
        customUserDetailsService.createUser(userJoinRequest, image);
        
        // 2. 사용자 JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(userJoinRequest.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    @Transactional
    public JwtTokenResponse updateJWT(String refreshToken) {
        // 1. refreshToken 검증
        jwtTokenProvider.validateToken(refreshToken);

        // 2. 사용자 식별 정보 추출
        String userName = jwtTokenProvider.getAuthentication(refreshToken).getName();

        // 3. redis에서 refreshToken 유효성 확인
        String storedRefreshToken = redisService.getRefreshToken(userName);
        if(storedRefreshToken.equals(refreshToken)){
            throw new InvalidTokenException(HttpStatus.BAD_REQUEST, "Refresh token is invalid or expired");
        }
        
        // 4. 새로운 accessToken과 refreshToken
        JwtTokenResponse tokens = tokenService.generateToken(userName, "");
        
        // 5. redis에 이전 refreshToken 삭제 후 저장
        redisService.deleteRefreshToken(userName); // 삭제
        redisService.saveRefreshToken(userName, tokens.getRefreshToken()); // 저장
        
        // 6. 재발급한 토큰들 응답
        return tokens;
    }

    @Override
    @Transactional
    public String updateFcmToken(String userName, String fcmToken) {
        // 1. 사용자 정보 찾기
        UserDto user = userDao.findByUserKakaoId(userName)
                // 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        // 2. 찾은 사용자에게 FCM 토큰 저장
        userDao.updateByUserFcmToken(userName, fcmToken);

        return "토큰이 성공적으로 저장되었습니다.";
    }
}
