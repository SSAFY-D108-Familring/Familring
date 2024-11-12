package com.familring.questionservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnockRequest {
    private Long questionId;
    private Long receiverId;

}
