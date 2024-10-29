package com.familring.userservice.model.dao;

import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserDeleteRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserDao {
    // 회원가입
    void insertUser(UserDto user);

    // 회원 정보 조회
    Optional<UserDto> findUserByUserKakaoId(@Param("userKakaoId") String userKakaoId);
    Optional<UserDto> findUserByUserId(@Param("userId") Long userId);

    // FCM 토큰 저장
    void updateUserFcmTokenByUserKakaoId(@Param("userName") String userName, @Param("fcmToken") String fcmToken);

    // 회원 탈퇴
    void deleteUser(UserDeleteRequest deleteRequest);
}
