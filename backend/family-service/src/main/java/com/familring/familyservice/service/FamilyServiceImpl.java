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

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FamilyServiceImpl implements FamilyService {

    private final JwtTokenProvider jwtTokenProvider;
    private final FamilyDao familyDao;

    @Override
    public FamilyInfoResponse getFamilyInfo(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyInfoByUserId(userId);

        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(familyDto.getFamilyId())
                .familyCode(familyDto.getFamilyCode())
                .familyCount(familyDto.getFamilyCount())
                .familyCommunicationStatus(familyDto.getFamilyCommunicationStatus())
                .build();

        return response;
    }

    @Override
    public String getFamilyCode(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 코드 조회
        String familyCode = familyDao.findFamilyInfoByUserId(userId).getFamilyCode();

        return familyCode;
    }

    @Override
    public List<UserInfoResponse> getFamilyMemberList(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 구성원 userId에 대해 user-service에게 사용자 정보 조회(GET "/users")  api 요청


        return List.of();
    }

    @Override
    public FamilyInfoResponse createFamily(String token) {
        // 1. 클레임에서 userId 추출
        Long userId = getUserId(token);

        // 2. 가족 생성

        // 3. 가족 구성원 추가

        // 4. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyInfoByUserId(userId);

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
