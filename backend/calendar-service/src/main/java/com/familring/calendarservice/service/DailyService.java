package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Daily;
import com.familring.calendarservice.dto.response.DailyDateResponse;
import com.familring.calendarservice.exception.daily.DailyNotFoundException;
import com.familring.calendarservice.exception.daily.InvalidDailyRequestException;
import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.DailyRepository;
import com.familring.calendarservice.service.client.FileServiceFeignClient;
import com.familring.common_service.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final FileServiceFeignClient fileServiceFeignClient;
    private final DailyRepository dailyRepository;

    @Value("${aws.s3.daily-photo-path}")
    private String dailyPhotoPath;

    public List<DailyDateResponse> getDailiesDateByMonth(int year, int month, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData()
                .getFamilyId();

        return dailyRepository.findDailiesByDateAndFamilyId(year, month, familyId).stream().map(
                daily -> DailyDateResponse.builder().id(daily.getId()).createdAt(daily.getCreatedAt()).build()).toList();
    }

    @Transactional
    public void createDaily(String content, MultipartFile image, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        List<MultipartFile> files = new ArrayList<>();
        files.add(image);
        String photoUrl = fileServiceFeignClient.uploadFiles(files, dailyPhotoPath).getData().get(0);

        Daily newDaily = Daily.builder().familyId(familyId).authorId(userId)
                .content(content).photoUrl(photoUrl).build();

        dailyRepository.save(newDaily);
    }

    @Transactional
    public void deleteDaily(Long dailyId, Long userId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow(DailyNotFoundException::new);

        if (!daily.getAuthorId().equals(userId)) {
            throw new InvalidDailyRequestException();
        }

        dailyRepository.delete(daily);
    }

    @Transactional
    public void updateDaily(String content, MultipartFile image, Long dailyId, Long userId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow(DailyNotFoundException::new);
        if (!daily.getAuthorId().equals(userId)) {
            throw new InvalidDailyRequestException();
        }

        daily.updateContent(content);

        if (image != null) {
            List<MultipartFile> uploadFileList = new ArrayList<>();
            uploadFileList.add(image);

            List<String> deleteFileList = new ArrayList<>();
            deleteFileList.add(daily.getPhotoUrl());

            // 비동기 요청을 위한 Future 생성
            CompletableFuture<BaseResponse<List<String>>> uploadFuture = CompletableFuture
                    .completedFuture(fileServiceFeignClient.uploadFiles(uploadFileList, dailyPhotoPath));
            CompletableFuture<BaseResponse<Void>> deleteFuture = CompletableFuture
                    .completedFuture(fileServiceFeignClient.deleteFiles(deleteFileList));


            // 두 요청이 모두 완료될 때까지 대기
            CompletableFuture.allOf(uploadFuture, deleteFuture).join();

            daily.updatePhotoUrl(uploadFuture.join().getData().get(0));
        }
    }
}
