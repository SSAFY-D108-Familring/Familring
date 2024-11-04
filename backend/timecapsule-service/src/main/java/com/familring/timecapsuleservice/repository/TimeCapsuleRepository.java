package com.familring.timecapsuleservice.repository;

import com.familring.timecapsuleservice.domain.TimeCapsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Integer> {

    @Query("SELECT t FROM TimeCapsule t WHERE t.startDate <= :currentDate AND t.endDate >= :currentDate AND t.familyId = :familyId")
    Optional<TimeCapsule> findTimeCapsuleWithinDateRangeAndFamilyId(@Param("currentDate") LocalDate currentDate, @Param("familyId") Long familyId);

}
