package com.familring.userservice.service;

import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import com.familring.userservice.model.dto.response.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    // 회원 정보 조회
    UserInfoResponse getUser(String userName);

    // 로그인
    JwtTokenResponse login(UserLoginRequest userLogInRequest);

    // 회원가입
    JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image);

    // JWT 재발급
    JwtTokenResponse updateJWT(String refreshToken);

    // FCM 토큰 저장
    String updateFcmToken(String userName, String fcmToken);

    // 회원 탈퇴
    String deleteUser(String name);
}
