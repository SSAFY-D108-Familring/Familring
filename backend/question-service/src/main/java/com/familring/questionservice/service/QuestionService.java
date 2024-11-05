package com.familring.questionservice.service;

import com.familring.questionservice.domain.Question;
import com.familring.questionservice.domain.QuestionFamily;
import com.familring.questionservice.dto.request.QuestionCreateResponse;
import com.familring.questionservice.exception.QuestionNotFoundException;
import com.familring.questionservice.repository.QuestionAnswerRepository;
import com.familring.questionservice.repository.QuestionFamilyRepository;
import com.familring.questionservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionFamilyRepository questionFamilyRepository;

    // 가족의 질문을 초기화하고 첫 번째 질문을 설정
    public void initializeQuestionFamily(Long familyId) {
        Question initialQuestion = questionRepository.findById(1L).orElseThrow(QuestionNotFoundException::new);
        QuestionFamily newQuestionFamily = QuestionFamily.builder()
                .familyId(familyId)
                .question(initialQuestion) // 초기 질문을 ID가 1인 질문으로 설정
                .build();
        questionFamilyRepository.save(newQuestionFamily);
    }

    // 랜덤 질문 생성
    private QuestionCreateResponse createQuestion(Long userId) {

        // userId로 가족 조회


        // 매일 9시에 questionRepository 에 있는 id 하나씩
        // questionFamilyRepository 에 넣어
        // 이때 넣을 때
        // 만약 가족 구성원이 답장을 다 했다면 (매일 9시에 확인함)
//        if(check(userId, questionId))
        // 그러면 questionId 값 1증가

        return QuestionCreateResponse.builder().build();
    }

    // 가족 구성원이 답장을 했는 지 확인
    private boolean check(Long userId, Long questionId) {
        // 가족 구성원이 다 답장했으면 return true;

        // userId로 가족 조회
        // 가족으로 가족 구성원 조회
        // 가족 구성원 모두가 questionAnswerRepository 에 모두가 다 있으면
        // return true;

        return false;
    }

}
