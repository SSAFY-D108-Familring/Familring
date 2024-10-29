package com.familring.familyservice.service;

import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

public interface FamilyService {
    // 가족 전체 정보 조회
    FamilyInfoResponse getFamilyInfo(String token);

    // 가족 코드 조회
    String getFamilyCode(String token);
}
