package com.familring.questionservice.repository;

import com.familring.questionservice.domain.QuestionFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionFamilyRepository extends JpaRepository<QuestionFamily, Integer> {
}
