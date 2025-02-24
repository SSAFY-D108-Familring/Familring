package com.familring.albumservice.dto.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyInfoResponse {
    private Long familyId;
    private String familyCode;
    private Integer familyCount;
    private Integer familyCommunicationStatus;
}