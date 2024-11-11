package com.familring.questionservice.service;

import com.familring.questionservice.domain.Question;
import com.familring.questionservice.domain.QuestionAnswer;
import com.familring.questionservice.domain.QuestionFamily;
import com.familring.questionservice.dto.client.Family;
import com.familring.questionservice.dto.client.UserInfoResponse;
import com.familring.questionservice.dto.request.QuestionAnswerCreateRequest;
import com.familring.questionservice.dto.request.QuestionAnswerUpdateRequest;
import com.familring.questionservice.dto.response.QuestionAnswerItem;
import com.familring.questionservice.dto.response.QuestionItem;
import com.familring.questionservice.dto.response.QuestionResponse;
import com.familring.questionservice.dto.response.QuestionListResponse;
import com.familring.questionservice.exception.*;
import com.familring.questionservice.repository.QuestionAnswerRepository;
import com.familring.questionservice.repository.QuestionFamilyRepository;
import com.familring.questionservice.repository.QuestionRepository;
import com.familring.questionservice.service.client.FamilyServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

            // 모두 답변했을 때는 포인트 증가

        } else {
            // 모두 답변 안했을 때는 답변 안했던 인원수만큼 포인트 감소

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
    public void updateQuestionAnswer(Long userId, QuestionAnswerUpdateRequest questionAnswerUpdateRequest) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가족의 현재 질문 조회
        QuestionFamily questionFamily = questionFamilyRepository.findByFamilyId(familyId)
                .orElseThrow(QuestionFamilyNotFoundException::new);

        Optional<QuestionAnswer> questionAnswer = questionAnswerRepository.findByQuestionFamilyAndUserId(questionFamily, userId);

        if (questionAnswer.isPresent()) {
            questionAnswer.get().updateQuestionAnswer(questionAnswerUpdateRequest);
            questionAnswerRepository.save(questionAnswer.get());
        } else {
            throw new QuestionAnswerNotFoundException();
        }

    }

    // 오늘의 랜덤 질문 조회
    public QuestionResponse getQuestion(Long userId, Long questionId) {

        // 가족 정보 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        Question question;
        QuestionFamily questionFamily;
        if (questionId!=null) {
            questionFamily = questionFamilyRepository.findByQuestionIdAndFamilyId(questionId, familyId).orElseThrow(QuestionFamilyNotFoundException::new);
            question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        } else {
            // 몇 번째 질문인지 (가족에 대한 질문 정보 가져오기)
            questionFamily = questionFamilyRepository.findByFamilyId(familyId).orElseThrow(QuestionFamilyNotFoundException::new);
            question = questionRepository.findById(questionFamily.getQuestion().getId()).orElseThrow(QuestionNotFoundException::new);
        }

        // 질문 답변 누구했는지
        // familyId 로 가족 구성원 조회
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberListByFamilyId(familyId).getData();

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
                    .userId(member.getUserId())
                    .userNickname(member.getUserNickname())
                    .userZodiacSign(member.getUserZodiacSign())
                    .userColor(member.getUserColor())
                    .answerContent(content)
                    .answerStatus(status)
                    .build();

            questionAnswerItemList.add(questionAnswerItem);
        }

        // 사용자 본인의 답변을 첫 번째로 이동
        int userIndex = -1;
        for (int i = 0; i < questionAnswerItemList.size(); i++) {
            if (questionAnswerItemList.get(i).getUserId().equals(userId)) {
                userIndex = i;
                break;
            }
        }

        if (userIndex > 0) { // 첫 번째가 아닐 경우에만 이동
            QuestionAnswerItem userAnswer = questionAnswerItemList.remove(userIndex);
            questionAnswerItemList.add(0, userAnswer);
        }

        return QuestionResponse.builder()
                .questionId(question.getId())
                .questionContent(question.getContent())
                .items(questionAnswerItemList)
                .build();

    }

    // 랜덤 질문 목록 전체 조회
    public QuestionListResponse getAllQuestions(Long userId, int pageNo, String order) {

        // 가족이 몇 번째 질문까지 했는지 확인해서
        // 가족 정보 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 몇 번째 질문인지 (가족에 대한 질문 정보 가져오기)
        QuestionFamily questionFamily = questionFamilyRepository.findByFamilyId(familyId)
                .orElseThrow(QuestionFamilyNotFoundException::new);
        Question question = questionRepository.findById(questionFamily.getQuestion().getId())
                .orElseThrow(QuestionNotFoundException::new);

        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(pageNo, 20); // 20개씩
        Slice<Question> questionSlice;

        // 최신순(내림차순) 또는 오래된순(오름차순) 정렬
        if (StringUtils.equals(order, "desc")) {
            questionSlice = questionRepository.findAllByIdLessThanEqualOrderByIdDesc(question.getId(), pageRequest);
        } else if (StringUtils.equals(order, "asc")){
            questionSlice = questionRepository.findAllByIdLessThanEqualOrderByIdAsc(question.getId(), pageRequest);
        } else {
            throw new InvalidQueryParamException();
        }

        List<QuestionItem> questionItems = questionSlice.getContent().stream()
                .map(q -> QuestionItem.builder()
                        .questionId(q.getId())
                        .questionContent(q.getContent())
                        .build())
                .toList();

        // QuestionListResponse 생성
        return QuestionListResponse.builder()
                .hasNext(questionSlice.hasNext())
                .isLast(questionSlice.isLast())
                .items(questionItems)
                .build();
    }

}
