package com.familring.interestservice.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestMissionListResponse {

    private int count; // 몇명이 참여했는지
    private List<InterestMissionItem> items; // 가족 구성원 인증샷 목록 조회

}
