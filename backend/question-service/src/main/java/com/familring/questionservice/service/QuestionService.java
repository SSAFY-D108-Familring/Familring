package com.familring.questionservice.service;

import com.familring.questionservice.domain.Question;
import com.familring.questionservice.domain.QuestionAnswer;
import com.familring.questionservice.domain.QuestionFamily;
import com.familring.questionservice.dto.client.Family;
import com.familring.questionservice.dto.client.UserInfoResponse;
import com.familring.questionservice.dto.request.QuestionAnswerCreateRequest;
import com.familring.questionservice.dto.request.QuestionAnswerUpdateRequest;
import com.familring.questionservice.dto.response.QuestionAnswerItem;
import com.familring.questionservice.dto.response.QuestionInfoResponse;
import com.familring.questionservice.exception.AlreadyExistQuestionAnswerException;
import com.familring.questionservice.exception.QuestionAnswerNotFoundException;
import com.familring.questionservice.exception.QuestionFamilyNotFoundException;
import com.familring.questionservice.exception.QuestionNotFoundException;
import com.familring.questionservice.repository.QuestionAnswerRepository;
import com.familring.questionservice.repository.QuestionFamilyRepository;
import com.familring.questionservice.repository.QuestionRepository;
import com.familring.questionservice.service.client.FamilyServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionFamilyRepository questionFamilyRepository;
    private final FamilyServiceFeignClient familyServiceFeignClient;

    // 가족의 질문을 초기화하고 첫 번째 질문을 설정
    public void initializeQuestionFamily(Long familyId) {
        Question initialQuestion = questionRepository.findById(1L).orElseThrow(QuestionNotFoundException::new);
        QuestionFamily newQuestionFamily = QuestionFamily.builder()
                .familyId(familyId)
                .question(initialQuestion) // 초기 질문을 ID가 1인 질문으로 설정
                .build();
        questionFamilyRepository.save(newQuestionFamily);
    }

    // 매일 9시에 자동으로 질문 생성
    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduledCreateQuestion() {
        // 모든 가족 조회
        List<Long> allFamilyIds = familyServiceFeignClient.getAllFamilyId().getData();

        for (Long familyId : allFamilyIds) {
            createQuestion(familyId);
        }
    }

    private void createQuestion(Long familyId) {
        // 가족별 현재 진행 중인 질문 정보 가져오기
        QuestionFamily questionFamily = questionFamilyRepository.findByFamilyId(familyId)
                .orElseThrow(QuestionFamilyNotFoundException::new);

        // 현재 질문 ID 가져오기
        Long currentQuestionId = questionFamily.getQuestion().getId();

        // 현재 질문에 가족 구성원이 모두 답변했는지 확인
        if (check(familyId, currentQuestionId)) {
            // 모두 답변했다면 다음 질문 설정
            Long nextQuestionId = currentQuestionId + 1;
            Question nextQuestion = questionRepository.findById(nextQuestionId)
                    .orElseThrow(QuestionNotFoundException::new);

            // QuestionFamily 업데이트
            questionFamily.updateQuestion(nextQuestion);
            questionFamilyRepository.save(questionFamily);
        }
    }

    // 가족 구성원이 답장을 했는 지 확인
    private boolean check(Long familyId, Long questionFamilyId) {
        // 1. 가족 구성원 조회 (familyId로 찾는 함수로 변경)
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberListByFamilyId(familyId).getData();

        // 2. 가족 구성원들이 모두 답변을 했는지 확인
        for (UserInfoResponse member : familyMembers) {
            boolean hasAnswer = questionAnswerRepository.existsByQuestionFamilyIdAndUserId(questionFamilyId, member.getUserId());
            if (!hasAnswer) {
                return false; // 아직 답변하지 않은 구성원이 있음
            }
        }

        return true; // 모든 구성원이 답변을 완료함
    }

    // 랜덤 질문 답변 작성
    public void createQuestionAnswer(Long userId, QuestionAnswerCreateRequest questionAnswerCreateRequest) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가족의 현재 질문 조회
        QuestionFamily questionFamily = questionFamilyRepository.findByFamilyId(familyId)
                .orElseThrow(QuestionFamilyNotFoundException::new);

        LocalDate now = LocalDate.now();
        // userId 랑 questionFamily 로 이미 작성된 답변이 있다면 Exception
        QuestionAnswer questionAnswer;

        boolean hasAnswer = questionAnswerRepository.existsByQuestionFamilyIdAndUserId(questionFamily.getId(), userId);

        if (!hasAnswer) { // 작성된 답변이 없으면
            questionAnswer = QuestionAnswer.builder()
                    .questionFamily(questionFamily)
                    .userId(userId)
                    .answer(questionAnswerCreateRequest.getContent())
                    .createdAt(now)
                    .modifiedAt(now)
                    .build();

            questionAnswerRepository.save(questionAnswer);
        } else {
            throw new AlreadyExistQuestionAnswerException();
        }
    }

    // 랜덤 질문 수정
    public void updateQuestionAnswer(Long answerId, QuestionAnswerUpdateRequest questionAnswerUpdateRequest) {

        Optional<QuestionAnswer> questionAnswer = questionAnswerRepository.findById(answerId);

        if (questionAnswer.isPresent()) {
            questionAnswer.get().updateQuestionAnswer(questionAnswerUpdateRequest);
            questionAnswerRepository.save(questionAnswer.get());
        } else {
            throw new QuestionAnswerNotFoundException();
        }

    }

    // 랜덤 질문 조회
    public QuestionInfoResponse getQuestionInfo(Long userId) {

        // 가족 정보 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 몇 번째 질문인지 (가족에 대한 질문 정보 가져오기)
        QuestionFamily questionFamily = questionFamilyRepository.findByFamilyId(familyId).orElseThrow(QuestionFamilyNotFoundException::new);
        Question question = questionRepository.findById(questionFamily.getQuestion().getId()).orElseThrow(QuestionNotFoundException::new);

        // 몇 번째 질문인지
        Long questionId = question.getId();
        // 질문 내용 뭔지
        String questionContent = question.getContent();

        // 질문 답변 누구했는지
        // familyId 로 가족 구성원 조회
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberList(familyId).getData();

        // question_answer 에 question_family_id 랑 user_id 로 확인해서
        // 있으면 true, 없으면 false
        List<QuestionAnswerItem> questionAnswerItemList = new ArrayList<>();
        QuestionAnswerItem questionAnswerItem;

        for (UserInfoResponse member : familyMembers) {
            Optional<QuestionAnswer> questionAnswerOpt = questionAnswerRepository.findByQuestionFamilyAndUserId(questionFamily, member.getUserId());

            boolean status;
            Long answerId = null;
            String content = null;
            QuestionAnswer questionAnswer;

            if (questionAnswerOpt.isPresent()) {
                questionAnswer = questionAnswerOpt.get();
                answerId = questionAnswer.getId();
                content = questionAnswer.getAnswer();
                status = true;
            } else {
                status = false;
            }

            questionAnswerItem = QuestionAnswerItem.builder()
                    .answerId(answerId)
                    .userNickname(member.getUserNickname())
                    .userZodiacSign(member.getUserZodiacSign())
                    .userColor(member.getUserColor())
                    .answerContent(content)
                    .answerStatus(status)
                    .build();

            questionAnswerItemList.add(questionAnswerItem);
        }

        return QuestionInfoResponse.builder()
                .questionId(questionId)
                .questionContent(questionContent)
                .items(questionAnswerItemList)
                .build();
    }

}
