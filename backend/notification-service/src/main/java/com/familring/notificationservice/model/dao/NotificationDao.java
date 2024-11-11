package com.familring.notificationservice.model.dao;

import com.familring.notificationservice.model.dto.Notification;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDao {
    List<Notification> findAllByReceiverId(@Param("userId") Long userId);
}
