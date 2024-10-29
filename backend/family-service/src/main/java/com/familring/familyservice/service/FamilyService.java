package com.familring.familyservice.service;

import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

public interface FamilyService {
    FamilyInfoResponse getFamilyInfo(String token);
}
