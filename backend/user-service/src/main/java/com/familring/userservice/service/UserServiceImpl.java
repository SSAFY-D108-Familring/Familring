package com.familring.userservice.service;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.token.InvalidRefreshTokenException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.FamilyRole;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserDeleteRequest;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import com.familring.userservice.model.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public UserInfoResponse getUser(String userName) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

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
    @Transactional
    public JwtTokenResponse login(UserLoginRequest userLogInRequest) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userLogInRequest.getUserKakaoId())
                // 2-1. 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userLogInRequest.getUserKakaoId()));

        // 2-2. 회원이 있는 경우 -> JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(user.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    @Transactional
    public JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image) {
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
        if (storedRefreshToken.equals(refreshToken)) {
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
    public String updateFcmToken(String userName, String fcmToken) {
        // 1. 사용자 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userName)
                // 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        // 2. 찾은 사용자에게 FCM 토큰 저장
        userDao.updateUserFcmTokenByUserKakaoId(userName, fcmToken);

        return "토큰이 성공적으로 저장되었습니다.";
    }

    @Override
    @Transactional
    public String deleteUser(String userName) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        // 2. redis의 refreshToken 제거
        redisService.deleteRefreshToken(userName);

        // 3. S3에서 이미지 제거

        // 4. kakaoId, 비밀번호, 별명, 가족 역할, 기분, fcm 토큰, 수정 일자, 탈퇴 여부 변경
        UserDeleteRequest deleteRequest = UserDeleteRequest.builder()
                .beforeUserKakaoId(userName)
                .afterUserKakaoId("DELETE_{" + user.getUserKakaoId() + "}")
                .userPassword("")
                .userName("탈퇴 회원")
                .userRole(FamilyRole.N)
                .userFace("")
                .userEmotion("")
                .userFcmToken("")
                .userIsDeleted(true)
                .build();

        // 5. user 테이블 수정
        userDao.deleteUser(deleteRequest);

        // 6. 가족 구성원 수 - 1

        return "회원 삭제 성공";
    }
}
