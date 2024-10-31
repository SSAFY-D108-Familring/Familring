package com.familring.userservice.service;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.token.InvalidRefreshTokenException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.FamilyRole;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.*;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import com.familring.userservice.model.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
    private final RestTemplate restTemplate = new RestTemplate();

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
    public JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image) throws IOException {
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
    public void deleteUser(Long userId) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserId(userId)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserId(" + userId + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2. redis의 refreshToken 제거
        redisService.deleteRefreshToken(user.getUserKakaoId());

        // 3. S3에서 이미지 제거
        // 3-1. file-service의 URL 설정
        String fileServiceUrl = "http://http://k11d108.p.ssafy.io/files";

        // 3-2. 삭제할 이미지 추가
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add(user.getUserFace());
        FileDeleteRequest request = FileDeleteRequest.builder()
                .fileUrls(fileUrls)
                .build();
        HttpEntity<FileDeleteRequest> fileEntity = new HttpEntity<>(request);

        // 3-3. DELETE 요청 전송
        ResponseEntity<Void> fileResponse = restTemplate.exchange(
                fileServiceUrl,
                HttpMethod.DELETE,
                fileEntity,
                Void.class
        );

        // 4. kakaoId, 비밀번호, 별명, 가족 역할, 기분, fcm 토큰, 수정 일자, 탈퇴 여부 변경
        UserDeleteRequest deleteRequest = UserDeleteRequest.builder()
                .userId(userId)
                .beforeUserKakaoId(user.getUserKakaoId())
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

        // 6. 가족 구성원 제거
        // 6-1. family-service의 URL 설정
        String familyServiceUrl = "http://http://k11d108.p.ssafy.io/family/member";

        // 6-2. Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString()); // 헤더에 userId 설정
        HttpEntity<String> familyEntity = new HttpEntity<>(headers);

        // 6-3. PATCH 요청 전송
        ResponseEntity<String> familyResponse = restTemplate.exchange(
                familyServiceUrl,
                HttpMethod.PATCH,
                familyEntity,
                String.class
        );
        log.info("response body: {}", familyResponse.getBody());
    }
}
