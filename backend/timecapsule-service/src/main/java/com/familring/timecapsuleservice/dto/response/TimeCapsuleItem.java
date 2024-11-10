package com.familring.timecapsuleservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleItem {
    private Long timeCapsuleId;
    private LocalDate date; // 타임캡슐 오픈일 (DB 에서는 마감일 endDate)
    private List<TimeCapsuleAnswerItem> items;
    private int index; // 타임캡슐의 순서
}
