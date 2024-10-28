package com.familring.userservice.model.dao;

import com.familring.userservice.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserDao {
    // 회원가입
    void insertByUser(UserDto user);

    // 회원 정보 조회
    Optional<UserDto> findByUserKakaoId(String userKakaoId);
}
