package com.familring.interestservice.repository;

import com.familring.interestservice.domain.Interest;
import com.familring.interestservice.domain.InterestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestAnswerRepository extends JpaRepository<InterestAnswer, Integer> {

    Optional<InterestAnswer> findByInterest(Interest interest);

}
