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

    @Column(name="interest_mission_photo_url")
    private LocalDate startDate;


}
