package com.familring.userservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface CustomUserDetailsService extends UserDetailsManager {
    // 회원 생성
    void createUser(UserDetails user);
    // 회원 정보 수정
    void updateUser(UserDetails user);
    // 회원 삭제
    void deleteUser(String username);
    // 회원의 oldPassword를 newPassword로 수정
    void changePassword(String oldPassword, String newPassword);
    // username을 사용해 회원 존재 확인
    boolean userExists(String username);
}
