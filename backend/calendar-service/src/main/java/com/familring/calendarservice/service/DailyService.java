package com.familring.calendarservice.service;

import com.familring.calendarservice.dto.response.DailyDateResponse;
import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.DailyRepository;
import com.familring.calendarservice.service.client.FileServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final FileServiceFeignClient fileServiceFeignClient;
    private final DailyRepository dailyRepository;

    public List<DailyDateResponse> getDailiesDateByMonth(int year, int month, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData()
                .getFamilyId();

        return dailyRepository.findDailiesByDateAndFamilyId(year, month, familyId).stream().map(
                daily -> DailyDateResponse.builder().id(daily.getId()).createdAt(daily.getCreatedAt()).build()).toList();
    }

    public void createDaily(String content, MultipartFile image, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();



    }
}
