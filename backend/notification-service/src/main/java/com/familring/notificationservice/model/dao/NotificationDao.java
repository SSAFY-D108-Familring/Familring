package com.familring.notificationservice.model.dao;

import com.familring.notificationservice.model.dto.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NotificationDao {
    // 전체 알림 리스트 조회
    List<Notification> findAllByReceiverId(@Param("userId") Long userId);

    // notificationId에 해당하는 Notification 반환
    Optional<Notification> findNotificationByNotificationId(@Param("notificationId") Long notificationId);

    void updateNotificationIsReadByNotificationId(@Param("notificationId") Long notificationId);
}
