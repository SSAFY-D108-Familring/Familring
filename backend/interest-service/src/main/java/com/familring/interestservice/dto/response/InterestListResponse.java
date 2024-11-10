package com.familring.interestservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestListResponse {
    int pageNo;
    boolean isLast; // 현재 슬라이스가 마지막인지 여부
    boolean hasNext; // 다음 슬라이스가 있는지 여부
    List<InterestItem> items;
}
