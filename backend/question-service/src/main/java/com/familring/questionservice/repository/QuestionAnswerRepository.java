package com.familring.questionservice.repository;

import com.familring.questionservice.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Integer> {

    boolean existsByQuestionFamilyIdAndUserId(Long questionFamilyId, Long userId);

    Optional<QuestionAnswer> findById(Long id);

}
