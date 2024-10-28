package com.familring.userservice.service;

import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final JwtTokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public JwtTokenResponse login(UserLoginRequest userLogInRequest) {
        // 회원 정보 찾기
        UserDto user = userDao.findByUserKakaoId(userLogInRequest.getUserKakaoId())
                // 회원이 없는 경우 -> 회원가입 처리
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userLogInRequest.getUserKakaoId()));

        // 회원이 있는 경우 -> JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(user.getUserKakaoId(), "");

        return tokens;
    }

    @Override
    public JwtTokenResponse join(UserJoinRequest userJoinRequest, MultipartFile image) throws IOException {
        // 사용자 회원가입
        customUserDetailsService.createUser(userJoinRequest, image);
        
        // 사용자 JWT 발급
        JwtTokenResponse tokens = tokenService.generateToken(userJoinRequest.getUserKakaoId(), "");

        return tokens;
    }
}
