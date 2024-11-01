package com.familring.familyservice.service;

import com.familring.familyservice.exception.family.FamilyNotFoundException;
import com.familring.familyservice.model.dao.FamilyDao;
import com.familring.familyservice.model.dto.FamilyDto;
import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FamilyServiceImpl implements FamilyService {

    private final FamilyDao familyDao;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public FamilyInfoResponse getFamilyInfo(Long userId) {
        // 1. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(familyDto.getFamilyId())
                .familyCode(familyDto.getFamilyCode())
                .familyCount(familyDto.getFamilyCount())
                .familyCommunicationStatus(familyDto.getFamilyCommunicationStatus())
                .build();

        // 3. 응답
        return response;
    }

    @Override
    public String getFamilyCode(Long userId) {
        // 1. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2.  가족 코드 조회
        String familyCode = familyDto.getFamilyCode();

        // 3. 응답
        return familyCode;
    }

    @Override
    public List<UserInfoResponse> getFamilyMemberList(Long userId) {
        // 1.  userId의 가족 구성원 모두의 userId 추출
        List<Long> members = familyDao.findFamilyUserByUserId(userId);

        // 2. 가족 구성원 userId에 대해 user-service에게 사용자 정보 조회(GET "/users")  api 요청
        List<UserInfoResponse> response = getUserInfoFromUserService(members);

        // 3. 응답
        return response;
    }
    public List<UserInfoResponse> getUserInfoFromUserService(List<Long> members) {
        // Feign Client를 통해 user-service의 getUser 메서드 호출
        return userServiceFeignClient.getAllUser(members);
    }

    @Override
    @Transactional
    public FamilyInfoResponse createFamily(Long userId) {
        // 1.  가족 생성
        // 1-1. 가족 코드 생성
        String code;
        do {
            String uuid = UUID.randomUUID().toString().toUpperCase();
            log.info("UUID: {}", uuid);
            code = uuid.substring(0, 6);
            log.info("code: {}", code);
        }
        // 1-2. 중복 확인
        while (familyDao.existsFamilyByFamilyCode(code));
        
        // 1-3. dto 수정
        FamilyCreateRequest familyCreateRequest = FamilyCreateRequest.builder()
                .familyCode(code)
                .familyCount(1)
                .familyCommunicationStatus(75)
                .build();

        // 1-4. DB에 가족 생성
        familyDao.insertFamily(familyCreateRequest);
        log.info("가족 생성 완료");

        // 1-5. 생성된 가족 familyId 찾기
        Long familyId = familyDao.findLastInsertedFamilyId();
        log.info("familyId: {}", familyId);

        // 2. 가족 구성원 추가
        familyDao.insetFamily_User(familyId, userId);
        log.info("가족 구성원 추가 완료");

        // 3. 가족 조회
        FamilyDto familyDto = familyDao.findFamilyByFamilyId(familyId)
                        .orElseThrow(() -> new FamilyNotFoundException());
        log.info("가족 조회 완료");

        // 4. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(familyDto.getFamilyId())
                .familyCode(familyDto.getFamilyCode())
                .familyCount(familyDto.getFamilyCount())
                .familyCommunicationStatus(familyDto.getFamilyCommunicationStatus())
                .build();

        // 5. 응답
        return response;
    }

    @Override
    @Transactional
    public String joinFamilyMember(Long userId, FamilyJoinRequest familyJoinRequest) {
        // 1. 가족 찾기
        FamilyDto familyDto = familyDao.findFamilyByFamilyCode(familyJoinRequest.getFamilyCode())
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2. 가족 구성원 추가
        familyDao.updateFamilyCountByFamilyId(familyDto.getFamilyId(), 1);
        familyDao.insetFamily_User(familyDto.getFamilyId(), userId);
        
        // 3. 응답
        return "가죽 구성원 추가 완료";
    }

    @Override
    @Transactional
    public String deleteFamilyMember(Long userId) {
        // 1. 회원에 해당하는 가족 찾기
        FamilyDto familyDto = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2. 가족 구성원 제거
        familyDao.updateFamilyCountByFamilyId(familyDto.getFamilyId(), -1);
        familyDao.deleteFamily_UserByFamilyIdAndUserId(familyDto.getFamilyId(), userId);

        return "가족 구성원 수정 완료";
    }
}
