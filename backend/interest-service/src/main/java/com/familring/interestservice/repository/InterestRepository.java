package com.familring.interestservice.repository;

import com.familring.interestservice.domain.Interest;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {

    Optional<Interest> findByIdAndFamilyId(Long id, Long familyId);

    // 가족에 가장 최근 관심사 찾기
    Optional<Interest> findFirstByFamilyIdOrderByIdDesc(Long familyId);

    Slice<Interest> findByFamilyIdOrderByIdDesc(Long familyId, Pageable pageable);

    int countByFamilyId(Long familyId);

    Optional<Interest> findFirstByFamilyIdAndMissionEndDateAfterOrMissionEndDateIsNullOrderByIdDesc(Long familyId, LocalDate currentDate);

}
