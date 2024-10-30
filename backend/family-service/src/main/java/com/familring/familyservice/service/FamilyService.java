package com.familring.familyservice.service;

import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.List;

public interface FamilyService {
    // 가족 전체 정보 조회
    FamilyInfoResponse getFamilyInfo(String token);

    // 가족 코드 조회
    String getFamilyCode(String token);

    // 가족 구성원 전체 조회
    List<UserInfoResponse> getFamilyMemberList(String token);

    // 가족 생성
    FamilyInfoResponse createFamily(String token);
}
