package com.familring.questionservice.repository;

import com.familring.questionservice.domain.QuestionFamily;
import com.familring.questionservice.dto.client.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionFamilyRepository extends JpaRepository<QuestionFamily, Integer> {

    Optional<QuestionFamily> findByFamilyId(Long familyId);

    Optional<QuestionFamily> findByQuestionIdAndFamilyId(Long questionId, Long familyId);

}
