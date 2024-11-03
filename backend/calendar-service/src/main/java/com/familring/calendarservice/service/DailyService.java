package com.familring.calendarservice.service;

import com.familring.calendarservice.dto.response.DailyDateResponse;
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

    public List<DailyDateResponse> getDailiesDateByMonth(int month, Long userId) {
        List<Long> userIds = familyServiceFeignClient.getFamilyMemberList(userId).getData()
                .stream().map(UserInfoResponse::getUserId).toList();

        return dailyRepository.findDailiesByMonthAndUserIdIn(month, userIds).stream().map(
                daily -> DailyDateResponse.builder().id(daily.getId()).createdAt(daily.getCreatedAt()).build()).toList();
    }
}
