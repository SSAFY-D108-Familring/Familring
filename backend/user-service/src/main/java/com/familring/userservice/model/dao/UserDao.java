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
    boolean existsUserByUserKakaoId(@Param("userKakaoId") String userKakaoId);

    // FCM 토큰 저장
    void updateUserFcmTokenByUserId(@Param("userId") Long userId, @Param("fcmToken") String fcmToken);

    // 회원 기분 변경
    void updateUserEmotionByUserId(@Param("userId") Long userId, @Param("userEmotion") String userEmotion);

    // 회원 닉네임 변경
    void updateUserNicknameByUserId(@Param("userId") Long userId,@Param("userNickname")  String userNickname);
    
    // 회원 탈퇴
    void deleteUser(UserDeleteRequest deleteRequest);
}
