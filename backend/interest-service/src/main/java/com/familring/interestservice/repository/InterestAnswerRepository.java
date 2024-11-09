package com.familring.interestservice.repository;

import com.familring.interestservice.domain.InterestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestAnswerRepository extends JpaRepository<InterestAnswer, Integer> {
}
