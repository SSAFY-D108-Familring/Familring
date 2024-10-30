package com.familring.familyservice.service;

import com.familring.familyservice.config.jwt.JwtTokenProvider;
import com.familring.familyservice.model.dao.FamilyDao;
import com.familring.familyservice.model.dto.FamilyDto;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FamilyServiceImpl implements FamilyService {

    private final JwtTokenProvider jwtTokenProvider;
    private final FamilyDao familyDao;
    private final RestTemplate restTemplate;

    @Override
    public FamilyInfoResponse getFamilyInfo(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyByUserId(userId);

        // 3. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(familyDto.getFamilyId())
                .familyCode(familyDto.getFamilyCode())
                .familyCount(familyDto.getFamilyCount())
                .familyCommunicationStatus(familyDto.getFamilyCommunicationStatus())
                .build();

        // 4. 응답
        return response;
    }

    @Override
    public String getFamilyCode(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 코드 조회
        String familyCode = familyDao.findFamilyByUserId(userId).getFamilyCode();

        // 3. 응답
        return familyCode;
    }

    @Override
    public List<UserInfoResponse> getFamilyMemberList(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. userId의 가족 구성원 모두의 userId 추출
        List<Long> members = familyDao.findFamilyUserByUserId(userId);

        // 3. 가족 구성원 userId에 대해 user-service에게 사용자 정보 조회(GET "/users/info")  api 요청
        List<UserInfoResponse> response = new ArrayList<>();

        for (Long memberId : members) {
            // user-service의 URL 설정
            String url = "http://user-service/users/info?userId=" + memberId;

            // 사용자 정보를 요청하여 리스트에 추가
            UserInfoResponse userInfo = restTemplate.getForObject(url, UserInfoResponse.class);
            response.add(userInfo);
        }

        // 4. 응답
        return response;
    }

    @Override
    public FamilyInfoResponse createFamily(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 생성

        // 3. 가족 구성원 추가

        // 4. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyByUserId(userId);

        // 5. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(familyDto.getFamilyId())
                .familyCode(familyDto.getFamilyCode())
                .familyCount(familyDto.getFamilyCount())
                .familyCommunicationStatus(familyDto.getFamilyCommunicationStatus())
                .build();

        // 6. 응답
        return response;
    }

    public Long getUserId(String token) {
        String accessToken = token.replace("Bearer ", "");

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        log.info("userId: {}", userId);

        return userId;
    }
}
