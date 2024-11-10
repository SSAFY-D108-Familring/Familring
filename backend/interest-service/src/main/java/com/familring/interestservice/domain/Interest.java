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
@Table(name = "interest")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="interest_id")
    private Long id;

    @Column(name="family_id")
    private Long familyId;

    @Column(name="interest_mission_end_date", nullable = true)
    private LocalDate missionEndDate;

    public void updateMissionEndDate(LocalDate newMissionEndDate) {
        this.missionEndDate = newMissionEndDate;
    }

}
