package com.familring.interestservice.repository;

import com.familring.interestservice.domain.InterestMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestMissionRepository extends JpaRepository<InterestMission, Integer> {
}
