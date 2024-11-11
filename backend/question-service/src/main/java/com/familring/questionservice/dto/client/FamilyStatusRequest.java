package com.familring.questionservice.dto.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyStatusRequest {
    private Long familyId;
    private int amount;
}