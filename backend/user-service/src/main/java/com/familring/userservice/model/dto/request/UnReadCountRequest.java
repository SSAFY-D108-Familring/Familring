package com.familring.userservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnReadCountRequest {
    private Long userId;
    private int amount;
}
