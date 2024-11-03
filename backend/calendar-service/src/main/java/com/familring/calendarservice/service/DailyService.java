package com.familring.calendarservice.service;

import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.service.client.dto.UserInfoResponse;
import com.familring.calendarservice.domain.Daily;
import com.familring.calendarservice.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final DailyRepository dailyRepository;

    // 3. 일상 조회는 유저 아이디로 가족 구성원 조회해서 디비에서 그 구성원들의 게시물 긁어오기
    public void getDailiesByMonth(int month, Long userId) {
        List<Long> userIds = familyServiceFeignClient.getFamilyMemberList(userId).getData()
                .stream().map(UserInfoResponse::getUserId).toList();

        List<Daily> dailies = dailyRepository.findDailiesByUserIdIn(userIds);


    }
}
