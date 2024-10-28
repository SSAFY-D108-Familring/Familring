package com.familring.userservice.model.dao;

import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserDao {
    // 로그인
    void login(UserLoginRequest userLoginRequest);

    // 회원가입
    void join(UserDto user);

    // 회원 정보 조회
    Optional<UserDto> findByUserKakaoId(String userKakaoId);
}
