package com.familring.familyservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Family {
    private Long familyId;
    private String familyCode;
    private Integer familyCount;
    private Integer familyCommunicationStatus;
}
