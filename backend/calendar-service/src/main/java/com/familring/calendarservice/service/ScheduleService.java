package com.familring.calendarservice.service;

import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final ScheduleRepository scheduleRepository;

    // 2. 일정 조회는 유저 아이디로 가족 id 조회해서 디비에서 긁어오기
    public void getSchedulesByMonth(int month, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        // 수정해
        scheduleRepository.findSchedulesByMonthAndUserIds(month, familyIds);
    }
}
