package com.familring.userservice.service;

import com.familring.userservice.config.s3.S3Service;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final S3Service s3Service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUserKakaoId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public UserDetails createUserDetails(UserDto userDto) {
        // UserDetails객체 생성
        return org.springframework.security.core.userdetails.User.builder()
                        .username(userDto.getUserKakaoId())
                        .password(userDto.getUserPassword())
                        .roles(userDto.isUserIsAdmin() ? "ADMIN" : "USER")
                        .build();
    }

    @Override
    @Transactional
    public void createUser(UserDetails user) {
        UserDto newUser = (UserDto) user;

        userDao.insertByUser(newUser);
    }

    @Override
    public void createUser(UserJoinRequest userJoinRequest, MultipartFile image){
        String faceImgUrl = null;

        // 프로필 사진 처리
        if(!image.isEmpty())
            faceImgUrl = s3Service.uploadS3(image, "user-face");

        // 띠 계산
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userJoinRequest.getUserBirthDate());
        int year = calendar.get(Calendar.YEAR);
        String[] zodiacSign = {"원숭이", "닭", "개", "돼지", "쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양"};
        int index = (year - 4) % 12;
        log.info("사용자의 띠: {}", zodiacSign[index]);

        // 띠 사진 처리
        String zodiacSignImgUrl = "/zodiac-sign/" + zodiacSign[index] + ".png";

        UserDto user = UserDto.builder()
                .userKakaoId(userJoinRequest.getUserKakaoId())
                .userPassword(passwordEncoder.encode(""))
                .userNickname(userJoinRequest.getUserNickname())
                .userBirthDate(userJoinRequest.getUserBirthDate())
                .userZodiacSign(zodiacSignImgUrl)
                .userRole(userJoinRequest.getUserRole())
                .userFace(faceImgUrl)
                .userColor(userJoinRequest.getUserColor())
                .userEmotion("")
                .userCreatedAt(LocalDateTime.now())
                .userModifiedAt(LocalDateTime.now())
                .userIsDeleted(false)
                .userIsAdmin(false)
                .build();

        // 기존 createUser(UserDetails user) 호출
        createUser(user);
    }

    @Override
    @Transactional
    public void updateUser(UserDetails user) {

    }

    @Override
    @Transactional
    public void deleteUser(String username) {

    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
