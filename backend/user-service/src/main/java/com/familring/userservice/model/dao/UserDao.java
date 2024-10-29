package com.familring.userservice.model.dao;

import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserDeleteRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserDao {
    // 회원가입
    void insertByUser(UserDto user);

    // 회원 정보 조회
    Optional<UserDto> findByUserKakaoId(String userKakaoId);

    // FCM 토큰 저장
    void updateByUserFcmToken(@Param("userName") String userName, @Param("fcmToken") String fcmToken);

    // 회원 탈퇴
    void delete(UserDeleteRequest deleteRequest);
}
