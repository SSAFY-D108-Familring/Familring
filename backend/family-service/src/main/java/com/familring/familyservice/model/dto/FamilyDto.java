package com.familring.familyservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FamilyDto {
    private Long familyId;
    private String familyCode;
    private Integer familyCount;
    private Integer familyCommunicationStatus;
    private LocalDateTime familyCreatedAt;
}
