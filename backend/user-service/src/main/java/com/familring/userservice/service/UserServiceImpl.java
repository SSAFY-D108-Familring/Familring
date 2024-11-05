package com.familring.userservice.service;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.token.InvalidRefreshTokenException;
import com.familring.userservice.exception.user.AlreadyUserException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.*;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import com.familring.userservice.model.dto.response.UserInfoResponse;
import jakarta.ws.rs.HEAD;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;

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
    public UserInfoResponse getUser(String userName) {
        log.info("userName: {}", userName);
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userName)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserKakaoId(" + userName + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });
        log.info("userNickname: {}", user.getUserNickname());

        // 2. 응답 빌더 생성
        UserInfoResponse response = UserInfoResponse.builder()
                .userId(user.getUserId())
                .userKakaoId(user.getUserKakaoId())
                .userNickname(user.getUserNickname())
                .userBirthDate(user.getUserBirthDate())
                .userZodiacSign(user.getUserZodiacSign())
                .userRole(user.getUserRole())
                .userFace(user.getUserFace())
                .userColor(user.getUserColor())
                .userEmotion(user.getUserEmotion())
                .build();

        // 3. 응답
        return response;
    }

    @Override
    public UserInfoResponse getUser(Long userId) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserId(userId)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserId(" + userId + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2. 응답 빌더 생성
        UserInfoResponse response = UserInfoResponse.builder()
                .userId(user.getUserId())
                .userKakaoId(user.getUserKakaoId())
                .userNickname(user.getUserNickname())
                .userBirthDate(user.getUserBirthDate())
                .userZodiacSign(user.getUserZodiacSign())
                .userRole(user.getUserRole())
                .userFace(user.getUserFace())
                .userColor(user.getUserColor())
                .userEmotion(user.getUserEmotion())
                .build();

        // 3. 응답
        return response;
    }

    @Override
    public List<UserInfoResponse> getAllUser(List<Long> userIds) {
        // 1. 응답 생성
        List<UserInfoResponse> responseList = new ArrayList<>();

        // 2. 각 userId별 메소드 호출
        for(Long userId : userIds) {
            responseList.add(getUser(userId));
        }

        // 3. 응답
        return responseList;
    }

    @Override
    @Transactional
    public JwtTokenResponse login(UserLoginRequest userLogInRequest) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userLogInRequest.getUserKakaoId())
                // 2-1. 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserKakaoId(" + userLogInRequest.getUserKakaoId() + ")로 회원을 찾을 수 없습니다. 회원가입을 진행해주세요!");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2-2. 회원이 있는 경우 -> JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(user.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    @Transactional
    public JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image) {
        // 1. 사용자 중복 확인
        if(userDao.existsUserByUserKakaoId(userJoinRequest.getUserKakaoId())) {
            throw new AlreadyUserException();
        }

        // 2. 사용자 회원가입
        customUserDetailsService.createUser(userJoinRequest, image);

        // 3. 사용자 JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(userJoinRequest.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    @Transactional
    public JwtTokenResponse updateJWT(String refreshToken) {
        // 1. refreshToken 검증
        jwtTokenProvider.validateToken(refreshToken);
        log.info("refreshToken 검증 완료");

        // 2. 사용자 식별 정보 추출
        String userName = jwtTokenProvider.getAuthentication(refreshToken).getName();
        log.info("userName: {}", userName);

        // 3-1. redis에서 refreshToken 가져오기
        String storedRefreshToken = redisService.getRefreshToken(userName);
        log.info("refreshToken 가져오기");

        // 3-2. 저장된 refreshToekn과 같은지 확인
        if (!storedRefreshToken.equals(refreshToken)) {
            // 3-3. 같지 않으면 만료된 토큰이라고 전달
            throw new InvalidRefreshTokenException();
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
    public void updateFcmToken(Long userId, String fcmToken) {
        // 1. 사용자 정보 찾기
        UserDto user = userDao.findUserByUserId(userId)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserId(" + userId + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2. 찾은 사용자에게 FCM 토큰 저장
        userDao.updateUserFcmTokenByUserId(user.getUserId(), fcmToken);
    }

    @Override
    @Transactional
    public void updateUserEmotion(Long userId, UserEmotionRequest userEmotionRequest) {
        // 1. 사용자 정보 찾기
        UserDto user = userDao.findUserByUserId(userId)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserId(" + userId + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2. 사용자의 기분 설정 변경
        userDao.updateUserEmotionByUserId(user.getUserId(), userEmotionRequest.getUserEmotion());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId){
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserId(userId)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserId(" + userId + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });
        // 2. 회원 탈퇴 작업 시행
        customUserDetailsService.deleteUser(user.getUserKakaoId());
    }
}
