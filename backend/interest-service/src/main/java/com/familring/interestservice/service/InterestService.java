package com.familring.interestservice.service;

import com.familring.interestservice.domain.Interest;
import com.familring.interestservice.domain.InterestAnswer;
import com.familring.interestservice.dto.client.Family;
import com.familring.interestservice.dto.client.UserInfoResponse;
import com.familring.interestservice.dto.request.InterestAnswerCreateRequest;
import com.familring.interestservice.dto.response.InterestAnswerItem;
import com.familring.interestservice.dto.response.InterestAnswerListResponse;
import com.familring.interestservice.dto.response.InterestAnswerMineResponse;
import com.familring.interestservice.dto.response.InterestAnswerSelectedResponse;
import com.familring.interestservice.exception.InterestAnswerNotFoundException;
import com.familring.interestservice.exception.AlreadyExistSelectInterestAnswerException;
import com.familring.interestservice.repository.InterestAnswerRepository;
import com.familring.interestservice.repository.InterestMissionRepository;
import com.familring.interestservice.repository.InterestRepository;
import com.familring.interestservice.service.client.FamilyServiceFeignClient;
import com.familring.interestservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.familring.interestservice.exception.InterestNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final InterestAnswerRepository interestAnswerRepository;
    private final InterestMissionRepository interestMissionRepository;
    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final UserServiceFeignClient userServiceFeignClient;

    // 관심사 답변 작성
    public void createInterestAnswer(Long userId, InterestAnswerCreateRequest interestAnswerCreateRequest) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 관심사
        Interest interest = Interest
                .builder()
                .familyId(familyId)
                .build();

        interestRepository.save(interest);

        // 관심사 답변
        InterestAnswer interestAnswer = InterestAnswer
                .builder()
                .familyId(familyId)
                .userId(userId)
                .interest(interest)
                .content(interestAnswerCreateRequest.getContent())
                .build();

        interestAnswerRepository.save(interestAnswer);

    }

    // 관심사 답변 수정
    public void updateInterestAnswer(Long userId, Long interestId, InterestAnswerCreateRequest interestAnswerCreateRequest) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 관심사 찾기
        Interest interest = interestRepository.findByIdAndFamilyId(interestId, familyId).orElseThrow(InterestNotFoundException::new);

        // 찾은 관심사로 관심사 답변 찾기
        InterestAnswer interestAnswer = interestAnswerRepository.findByInterest(interest).orElseThrow(InterestAnswerNotFoundException::new);

        // 수정
        interestAnswer.updateContent(interestAnswerCreateRequest.getContent());

    }

    // 관심사 답변 작성 유무
    public boolean getInterestAnswerStatus(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 그 가족의 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 내가 작성한 관심사 답변 찾기
        Optional<InterestAnswer> interestAnswer = interestAnswerRepository.findByUserIdAndInterest(userId, interest);

        return interestAnswer.isPresent(); // 있으면 true, 없으면 false

    }

    // 관심사 답변 목록 조회
    public InterestAnswerListResponse getInterestAnswerList(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 가족 구성원 찾기
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberList(userId).getData();

        List<InterestAnswerItem> interestAnswerItems = new ArrayList<>();

        for (UserInfoResponse familyMember : familyMembers) {

            // 구성원이 답변 했는지 확인
            Optional<InterestAnswer> interestAnswer = interestAnswerRepository.findByUserIdAndInterest(familyMember.getUserId(), interest);

            String content = null;

            // 답변 했으면 content 채워주기
            if (interestAnswer.isPresent()) { // 존재하면
                content = interestAnswer.get().getContent();
            }

            // 가족 구성원 답변 정보 반환
            InterestAnswerItem interestAnswerItem = InterestAnswerItem.builder()
                    .userId(familyMember.getUserId())
                    .userNickname(familyMember.getUserNickname())
                    .userZodiacSign(familyMember.getUserZodiacSign())
                    .content(content)
                    .build();

            interestAnswerItems.add(interestAnswerItem);
        }

        return InterestAnswerListResponse
                .builder()
                .items(interestAnswerItems)
                .build();

    }

    // 내가 작성한 관심사 조회
    public InterestAnswerMineResponse getInterestAnswerMine(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 내가 작성한 관심사 답변 찾기
        InterestAnswer interestAnswer = interestAnswerRepository.findByUserIdAndInterest(userId, interest).orElseThrow(InterestAnswerNotFoundException::new);

        return InterestAnswerMineResponse
                .builder()
                .content(interestAnswer.getContent())
                .build();

    }

    // 선택된 관심사 조회
    public InterestAnswerSelectedResponse getInterestAnswerSelected(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 가족 구성원 찾기
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberList(userId).getData();

        // 답변한 가족 구성원 리스트 생성 및 모든 답변의 selected 상태 확인
        List<InterestAnswer> interestAnswers = familyMembers.stream()
                .map(member -> interestAnswerRepository.findByUserIdAndInterest(member.getUserId(), interest))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        boolean hasSelectedAnswer = interestAnswers.stream().anyMatch(InterestAnswer::isSelected);

        // 랜덤으로 선택하여 selected 상태 업데이트
        if (!hasSelectedAnswer && !interestAnswers.isEmpty()) {
            InterestAnswer selectedAnswer = interestAnswers.get(new Random().nextInt(interestAnswers.size()));
            selectedAnswer.updateSelected(true);
            interestAnswerRepository.save(selectedAnswer);  // 변경 사항을 저장

            // 선택된 답변의 사용자 정보 조회
            UserInfoResponse selectedUser = familyMembers.stream()
                    .filter(member -> member.getUserId().equals(selectedAnswer.getUserId()))
                    .findFirst()
                    .orElseThrow(AlreadyExistSelectInterestAnswerException::new);

            return InterestAnswerSelectedResponse.builder()
                    .userNickname(selectedUser.getUserNickname())
                    .content(selectedAnswer.getContent())
                    .build();
        }

        throw new AlreadyExistSelectInterestAnswerException();

    }

    //

}
