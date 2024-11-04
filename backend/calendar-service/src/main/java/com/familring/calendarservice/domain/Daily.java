package com.familring.calendarservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "daily")
public class Daily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;

    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "author_id")
    private Long authorId;

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
