package com.familring.timecapsuleservice.dto.response;

import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleStatusResponse {

    private int status;
    private int dayCount; // 남은 날짜
    private int count; // 몇 번째 타임캡슐 인지
    private List<UserInfoResponse> users; // 작성한 가족 구성원 목록

}
