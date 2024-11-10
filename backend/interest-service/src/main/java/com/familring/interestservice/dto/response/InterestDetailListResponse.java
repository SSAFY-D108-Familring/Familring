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
public class InterestDetailListResponse {

    List<InterestDetailItem> items;

}
