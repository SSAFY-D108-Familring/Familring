package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Daily;
import com.familring.calendarservice.dto.client.UserInfoResponse;
import com.familring.calendarservice.dto.response.DailyDateResponse;
import com.familring.calendarservice.dto.response.DailyResponse;
import com.familring.calendarservice.exception.daily.DailyNotFoundException;
import com.familring.calendarservice.exception.daily.InvalidDailyRequestException;
import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.DailyRepository;
import com.familring.calendarservice.service.client.FileServiceFeignClient;
import com.familring.calendarservice.service.client.UserServiceFeignClient;
import com.familring.common_module.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final FileServiceFeignClient fileServiceFeignClient;
    private final UserServiceFeignClient userServiceFeignClient;
    private final DailyRepository dailyRepository;

    @Value("${aws.s3.daily-photo-path}")
    private String dailyPhotoPath;

    public List<DailyDateResponse> getDailiesByYearAndMonth(int year, int month, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData()
                .getFamilyId();

        return dailyRepository.findByYearAndMonthAndFamilyId(year, month, familyId).stream().map(
                daily -> DailyDateResponse.builder().id(daily.getId()).createdAt(daily.getCreatedAt()).build()).toList();
    }

    @Transactional
    public void createDaily(String content, MultipartFile image, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        List<MultipartFile> files = new ArrayList<>();
        files.add(image);
        String photoUrl = fileServiceFeignClient.uploadFiles(files, getDailyPhotoPath(familyId)).getData().get(0);

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
        List<String> deleteUrls = new ArrayList<>();
        deleteUrls.add(daily.getPhotoUrl());
        fileServiceFeignClient.deleteFiles(deleteUrls);
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
                    .completedFuture(fileServiceFeignClient.uploadFiles(uploadFileList, getDailyPhotoPath(daily.getFamilyId())));
            CompletableFuture<BaseResponse<Void>> deleteFuture = CompletableFuture
                    .completedFuture(fileServiceFeignClient.deleteFiles(deleteFileList));


            // 두 요청이 모두 완료될 때까지 대기
            CompletableFuture.allOf(uploadFuture, deleteFuture).join();

            daily.updatePhotoUrl(uploadFuture.join().getData().get(0));
        }
    }

    private String getDailyPhotoPath(Long familyId) {
        return dailyPhotoPath + "/" + familyId;
    }

    public List<DailyResponse> getDailies(List<Long> dailyIds, Long userId) {
        List<Daily> dailies = dailyRepository.findAllById(dailyIds);

        Map<Long, UserInfoResponse> userMap = userServiceFeignClient
                .getAllUser(dailies.stream().map(Daily::getAuthorId).distinct().toList()).getData()
                .stream().collect(Collectors.toMap(UserInfoResponse::getUserId, u -> u));

        return dailies.stream().map(daily -> {
            UserInfoResponse userInfo = userMap.get(daily.getAuthorId());

            return DailyResponse.builder().id(daily.getId()).content(daily.getContent()).photoUrl(daily.getPhotoUrl())
                    .userNickname(userInfo.getUserNickname()).userZodiacSign(userInfo.getUserZodiacSign())
                    .userColor(userInfo.getUserColor()).myPost(daily.getAuthorId().equals(userId)).build();
        }).toList();
    }

    public List<DailyResponse> getDailiesByDate(int year, int month, int day, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData()
                .getFamilyId();

        LocalDate date = LocalDate.of(year, month, day);
        List<Daily> dailies = dailyRepository.findByDateAndFamilyId(date, familyId);

        Map<Long, UserInfoResponse> userMap = userServiceFeignClient
                .getAllUser(dailies.stream().map(Daily::getAuthorId).distinct().toList()).getData()
                .stream().collect(Collectors.toMap(UserInfoResponse::getUserId, u -> u));

        return dailies.stream().map(daily -> {
            UserInfoResponse userInfo = userMap.get(daily.getAuthorId());

            return DailyResponse.builder().id(daily.getId()).content(daily.getContent()).photoUrl(daily.getPhotoUrl())
                    .userNickname(userInfo.getUserNickname()).userZodiacSign(userInfo.getUserZodiacSign())
                    .userColor(userInfo.getUserColor()).myPost(daily.getAuthorId().equals(userId)).build();
        }).toList();
    }
}
