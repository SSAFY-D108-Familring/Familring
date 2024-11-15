package com.familring.timecapsuleservice.repository;

import com.familring.timecapsuleservice.domain.TimeCapsule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Integer> {

    Optional<TimeCapsule> findByFamilyId(Long familyId);

    Optional<TimeCapsule> findFirstByFamilyIdOrderByIdDesc(Long familyId);

    @Query("SELECT t FROM TimeCapsule t WHERE t.startDate <= :currentDate AND t.endDate >= :currentDate AND t.familyId = :familyId")
    Optional<TimeCapsule> findTimeCapsuleWithinDateRangeAndFamilyId(@Param("currentDate") LocalDate currentDate, @Param("familyId") Long familyId);

    int countByFamilyId(Long familyId);

    // 한 가족이 생성했던 타임 캡슐 전체 조회
    Slice<TimeCapsule> findTimeCapsulesByFamilyIdOrderByStartDateDesc(Long familyId, Pageable pageable);

}
