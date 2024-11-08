package com.familring.calendarservice.dto.response;

import com.familring.calendarservice.dto.client.FamilyRole;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DailyResponse {
    private Long id;
    private Boolean myPost;
    private String content;
    private String photoUrl;
    private String userNickname;
    private String userZodiacSign;
    private String userColor;
}