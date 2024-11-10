package com.familring.interestservice.repository;

import com.familring.interestservice.domain.Interest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {

    Optional<Interest> findByIdAndFamilyId(Long id, Long familyId);

    // 가족에 가장 최근 관심사 찾기
    Optional<Interest> findFirstByFamilyId(Long familyId);

    Slice<Interest> findByFamilyIdOrderByIdDesc(Long familyId, Pageable pageable);

    int countByFamilyId(Long familyId);

}
