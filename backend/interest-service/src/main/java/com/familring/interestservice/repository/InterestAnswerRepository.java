package com.familring.interestservice.repository;

import com.familring.interestservice.domain.Interest;
import com.familring.interestservice.domain.InterestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestAnswerRepository extends JpaRepository<InterestAnswer, Integer> {

    Optional<InterestAnswer> findByInterest(Interest interest);

    Optional<InterestAnswer> findByUserIdAndInterest(Long userId, Interest interest);

    List<InterestAnswer> findByFamilyIdAndInterest(Long familyId, Interest interest);

    @Query("SELECT ia FROM InterestAnswer ia WHERE ia.familyId = :familyId AND ia.interest = :interest AND ia.selected = true")
    Optional<InterestAnswer> findSelectedAnswersByFamilyIdAndInterest(@Param("familyId") Long familyId, @Param("interest") Interest interest);

}
