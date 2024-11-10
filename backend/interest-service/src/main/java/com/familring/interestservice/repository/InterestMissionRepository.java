package com.familring.interestservice.repository;

import com.familring.interestservice.domain.Interest;
import com.familring.interestservice.domain.InterestMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestMissionRepository extends JpaRepository<InterestMission, Integer> {

    Optional<InterestMission> findByInterestAndUserId(Interest interest, Long userId);

}
