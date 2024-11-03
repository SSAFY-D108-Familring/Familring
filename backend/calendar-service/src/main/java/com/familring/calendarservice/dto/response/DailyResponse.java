package com.familring.calendarservice.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class DailyResponse {
    private Long id;
    private
}

public class Daily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "daily_content")
    private String content;

    @Column(name = "daily_photo_url")
    private String photoUrl;

    @CreatedDate
    @Column(updatable = false, name = "daily_created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "daily_modified_at")
    private LocalDateTime modifiedAt;
}

