package com.familring.userservice.service;

import com.familring.userservice.model.dto.request.UserJoinRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomUserDetailsService extends UserDetailsManager {
    // 회원 정보 로딩
    UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException;

    // 회원 생성
    void createUser(UserDetails user);
    void createUser(UserJoinRequest userJoinRequest, MultipartFile image) throws IOException;

    // 회원 정보 수정
    void updateUser(UserDetails user);

    // 회원 삭제
    void deleteUser(String username);

    // 회원의 oldPassword를 newPassword로 수정
    void changePassword(String oldPassword, String newPassword);

    // username을 사용해 회원 존재 확인
    boolean userExists(String username);
}
