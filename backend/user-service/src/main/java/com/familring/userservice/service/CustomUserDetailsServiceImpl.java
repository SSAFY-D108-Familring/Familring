package com.familring.userservice.service;

import com.familring.userservice.exception.user.NoContentUserImageException;
import com.familring.userservice.model.dao.UserDao;
import com.familring.userservice.model.dto.UserDto;
import com.familring.userservice.model.dto.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RestTemplate restTemplate = new RestTemplate();

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
    public void createUser(UserJoinRequest userJoinRequest, MultipartFile image) throws IOException {
        String fileServiceUrl = "http://http://k11d108.p.ssafy.io/files";
        String faceImgUrl = "";

        // 얼굴 사진 처리
        if(image.isEmpty()){
            throw new NoContentUserImageException();
        }

        // List<MultipartFile>로 파일 리스트 구성
        List<MultipartFile> faceFiles = new ArrayList<>();
        faceFiles.add(image);

        // MultipartBodyBuilder로 요청 구성
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        for (MultipartFile file : faceFiles) {
            builder.part("files", new ByteArrayResource(file.getBytes()))
                    .header("Content-Disposition", "form-data; name=files; filename=" + file.getOriginalFilename())
                    .contentType(MediaType.MULTIPART_FORM_DATA);
        }
        HttpEntity<?> faceEntity = new HttpEntity<>(builder.build());

        // POST 요청 전송
        ResponseEntity<List> response = restTemplate.exchange(
                fileServiceUrl,
                HttpMethod.POST,
                faceEntity,
                List.class
        );

        // 요청에 대한 응답 image url 저장
        faceImgUrl = response.getBody().get(0).toString();

        // 날짜 받기
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userJoinRequest.getUserBirthDate());
        int birthYear = calendar.get(Calendar.YEAR);

        // 띠 배열 선언
        String[] zodiacSign = {"쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지"};

        // 띠 계산
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
