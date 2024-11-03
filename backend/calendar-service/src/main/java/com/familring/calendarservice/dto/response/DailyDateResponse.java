package com.familring.calendarservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyDateResponse {
    private Long id;
    private LocalDateTime createdAt;
}

