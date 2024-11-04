package com.familring.calendarservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "schedule_start_time")
    private LocalDateTime startTime;

    @Column(name = "schedule_end_time")
    private LocalDateTime endTime;

    @Column(name = "schedule_title")
    private String title;

    @Column(name = "schedule_has_notification")
    private Boolean hasNotification;

    @Column(name = "schedule_has_time")
    private Boolean hasTime;

    @Column(name = "schedule_color")
    private String color;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleUser> scheduleUsers = new ArrayList<>();

    public void addUser(Long attendeeId, Boolean attendanceStatus) {
        ScheduleUser scheduleUser = ScheduleUser.builder()
                .schedule(this)
                .attendeeId(attendeeId)
                .attendanceStatus(attendanceStatus)
                .build();
        this.scheduleUsers.add(scheduleUser);
    }

    public void removeUser(Long userId) {
        this.scheduleUsers.removeIf(su -> su.getAttendeeId().equals(userId));
    }

    public void updateSchedule(LocalDateTime startTime, LocalDateTime endTime,
                               String title, Boolean hasNotification, Boolean hasTime, String color) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.hasNotification = hasNotification;
        this.hasTime = hasTime;
        this.color = color;
    }
}