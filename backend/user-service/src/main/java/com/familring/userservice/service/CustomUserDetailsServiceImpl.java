package com.familring.userservice.service;

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

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findUserByUserKakaoId(username)
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

        userDao.insertUser(newUser);
    }

    @Override
    @Transactional
    public void createUser(UserJoinRequest userJoinRequest, MultipartFile image) {
        String faceImgUrl = "";

        // 나중에 file-service로 업로드 하도록 수정
        // 프로필 사진 처리
//        if(!image.isEmpty())
//            faceImgUrl = s3Service.uploadS3(image, "user-face");

        // 띠 계산
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userJoinRequest.getUserBirthDate());
        int year = calendar.get(Calendar.YEAR);
        // 띠 배열의 시작을 "쥐"로 변경하여 정확한 계산이 가능하도록 설정
        String[] zodiacSign = {"쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지"};

        // 띠 계산
        int birthYear = calendar.get(Calendar.YEAR);
        int zodiacIndex = (birthYear - 4) % 12;

        // 음수 인덱스 방지
        if (zodiacIndex < 0)
            zodiacIndex += 12;

        log.info("사용자의 띠: {}", zodiacSign[zodiacIndex]);

        // 띠 이미지 URL 생성
        String zodiacSignImgUrl = "/zodiac-sign/" + zodiacSign[zodiacIndex] + ".png";

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
