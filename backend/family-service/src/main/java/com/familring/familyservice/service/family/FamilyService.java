package com.familring.familyservice.service.family;

import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.request.FamilyStatusRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;

import java.util.List;

public interface FamilyService {
    // 가족 전체 정보 조회
    FamilyInfoResponse getFamilyInfo(Long userId);

    // 가족 코드 조회
    String getFamilyCode(Long userId);

    // 가족 구성원 전체 조회 - userId
    List<UserInfoResponse> getFamilyMemberList(Long userId);

    // 가족 구성원 전체 조회 - familyId
    List<UserInfoResponse> getFamilyMemberListByFamilyId(Long familyId);

    // 모든 가족 조회
    List<Long> getAllFamilyId();

    // 모든 가족 수 조회
    int getAllFamilyCount(Long userId);

    // 가족 코드 유효성 검사
    boolean validateFamilyCode(String familyCode);

    // 가족 구성원 불가능 역할 조회
    List<String> validFamilyMember(String familyCode);

    // 가족 생성
    FamilyInfoResponse createFamily(Long userId);

    // 가족 구성원 추가
    String joinFamilyMember(Long userId, FamilyJoinRequest familyJoinRequest);

    // 가족 구성원 제거
    String deleteFamilyMember(Long userId);

    // 가족 상태 변경
    void updateFamilyStatus(FamilyStatusRequest familyStatusRequest);
}
