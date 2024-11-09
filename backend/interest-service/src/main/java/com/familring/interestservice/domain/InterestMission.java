package com.familring.interestservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "interest_mission")
public class InterestMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="interest_mission_id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;

    @Column(name="interest_mission_photo_url")
    private String photoUrl;

    @Column(name="interest_mission_created_at")
    private LocalDate createdAt;
}
