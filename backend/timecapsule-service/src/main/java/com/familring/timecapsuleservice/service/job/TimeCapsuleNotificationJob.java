package com.familring.timecapsuleservice.service.job;

import com.familring.timecapsuleservice.dto.client.NotificationRequest;
import com.familring.timecapsuleservice.dto.client.NotificationType;
import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import com.familring.timecapsuleservice.service.client.FamilyServiceFeignClient;
import com.familring.timecapsuleservice.service.client.NotificationServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class TimeCapsuleNotificationJob implements Job {

    private final NotificationServiceFeignClient notificationServiceFeignClient;
    private final FamilyServiceFeignClient familyServiceFeignClient;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long userId = context.getJobDetail().getJobDataMap().getLong("userId");

        // 가족 구성원 조회
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberList(userId).getData();
        List<Long> familyMemberIds = new ArrayList<>();

        for (UserInfoResponse familyMember : familyMembers) {
            familyMemberIds.add(familyMember.getUserId());
        }

        // 알림 메시지 생성
        String message = "가족들의 속마음은 무엇일까요? 타임캡슐에서 확인해보세요 !";
        NotificationRequest request = NotificationRequest.builder()
                .notificationType(NotificationType.TIMECAPSULE)
                .receiverUserIds(familyMemberIds)
                .senderUserId(null)
                .destinationId(null)
                .title("타임캡슐이 열렸습니다 \uD83D\uDC8A ")
                .message(message)
                .build();

        // 알림 전송
        notificationServiceFeignClient.alarmByFcm(request);
    }

}
