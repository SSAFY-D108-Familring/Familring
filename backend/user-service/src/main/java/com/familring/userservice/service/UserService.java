package com.familring.userservice.service;

import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    // 로그인
    JwtTokenResponse login(UserLoginRequest userLogInRequest);

    // 회원가입
    JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image) throws IOException;
}
