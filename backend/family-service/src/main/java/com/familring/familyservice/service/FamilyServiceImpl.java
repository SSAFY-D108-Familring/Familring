package com.familring.familyservice.service;

import com.familring.familyservice.config.jwt.JwtTokenProvider;
import com.familring.familyservice.model.dao.FamilyDao;
import com.familring.familyservice.model.dto.FamilyDto;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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

    public Long getUserId(String token) {
        String accessToken = token.replace("Bearer ", "");

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        log.info("userId: {}", userId);

        return userId;
    }
}
