package com.familring.familyservice.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyCreateRequest {
    private String familyCode;
    private Integer familyCount;
    private Integer familyCommunicationStatus;
}
