package com.familring.userservice.service;

import com.familring.userservice.config.redis.RedisService;
import com.familring.userservice.exception.user.NoContentUserImageException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.FamilyRole;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserDeleteRequest;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.service.client.FamilyServiceFeignClient;
import com.familring.userservice.service.client.FileServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final FileServiceFeignClient fileServiceFeignClient;
    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final RedisService redisService;

    @Value("${cloud.aws.s3.url}")
    private String s3Url;

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

        // 1. 얼굴 사진 처리
        if (image.isEmpty()) {
            // 1-1. 얼굴 사진 없는 경우 에러 처리
            throw new NoContentUserImageException();
        }

        // 1-2. 요청에 대한 응답 image url 저장
        faceImgUrl = uploadFiles(image, "user-face").get(0);
        log.info("faceImgUrl: {}", faceImgUrl);

        // 2. 회원 띠 계산
        // 2-1. 날짜 받기
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userJoinRequest.getUserBirthDate());
        int birthYear = calendar.get(Calendar.YEAR);

        // 2-2. 띠 배열 선언
        String[] zodiacSign = {"쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지"};

        // 2-3. 띠 계산
        int zodiacIndex = (birthYear - 4) % 12;

        // 2-4. 음수 인덱스 방지
        if (zodiacIndex < 0)
            zodiacIndex += 12;
        log.info("사용자의 띠: {}", zodiacSign[zodiacIndex]);

        // 2-5. 띠 이미지 URL 가져오기
        String zodiacSignImgUrl = s3Url + "/zodiac-sign/" + zodiacSign[zodiacIndex] + ".png";
        log.info("zodiacSignImgUrl: {}", zodiacSignImgUrl);


        // 3. 회원 가입 dto 생성
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

        // 4. 기존 createUser(UserDetails user) 호출
        createUser(user);
    }

    public List<String> uploadFiles(MultipartFile image, String folderPath) {
        log.info("folderPath: {}", folderPath);

        // List<MultipartFile>로 파일 리스트 구성
        List<MultipartFile> faceFiles = List.of(image);

        // Feign Client로 파일 업로드 요청
        List<String> response = fileServiceFeignClient.uploadFiles(faceFiles, folderPath).getData();

        return response;
    }

    @Override
    @Transactional
    public void updateUser(UserDetails user) {

    }

    @Override
    @Transactional
    public void deleteUser(String userName) {
        // 1. 회원 정보 찾기
        UserDto user = userDao.findUserByUserKakaoId(userName)
                .orElseThrow(() -> {
                    UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("UserKakaoId(" + userName + ")로 회원을 찾을 수 없습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, usernameNotFoundException.getMessage(), usernameNotFoundException);
                });

        // 2. redis의 refreshToken 제거
        redisService.deleteRefreshToken(user.getUserKakaoId());
        log.info("redis refreshToken 제거 완료");

        // 3. S3에서 이미지 제거
        log.info("userFace: {}", user.getUserFace());
        deleteFiles(user.getUserFace());
        log.info("S3에서 회원 얼굴 이미지 제거 완료");

        // 4. kakaoId, 비밀번호, 별명, 가족 역할, 기분, fcm 토큰, 수정 일자, 탈퇴 여부 변경
        UserDeleteRequest deleteRequest = UserDeleteRequest.builder()
                .userId(user.getUserId())
                .beforeUserKakaoId(userName)
                .afterUserKakaoId("DELETE_{" + userName + "}")
                .userPassword("")
                .userNickname("탈퇴 회원")
                .userRole(FamilyRole.N)
                .userFace("")
                .userEmotion("")
                .userFcmToken("")
                .userIsDeleted(true)
                .build();

        // 5. user 테이블 수정
        userDao.deleteUser(deleteRequest);
        log.info("user 테이블 수정 완료");

        // 6. 가족 구성원 제거
        String familyResponse = deleteFamilyMember(user.getUserId());
        log.info(familyResponse);
    }

    public void deleteFiles(String imageUrl) {
        // List<MultipartFile>로 파일 리스트 구성
        List<String> faceFiles = List.of(imageUrl);

        // Feign Client로 파일 삭제 요청
        fileServiceFeignClient.deleteFiles(faceFiles);
    }

    public String deleteFamilyMember(Long userId) {
        familyServiceFeignClient.deleteFamilyMember(userId);

        return "file-service에서 파일 삭제 완료";
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
