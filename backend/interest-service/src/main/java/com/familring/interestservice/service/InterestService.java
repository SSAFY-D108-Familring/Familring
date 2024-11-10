package com.familring.interestservice.service;

import com.familring.interestservice.domain.Interest;
import com.familring.interestservice.domain.InterestAnswer;
import com.familring.interestservice.domain.InterestMission;
import com.familring.interestservice.dto.client.Family;
import com.familring.interestservice.dto.client.UserInfoResponse;
import com.familring.interestservice.dto.request.InterestAnswerCreateRequest;
import com.familring.interestservice.dto.request.InterestMissionCreatePeriodRequest;
import com.familring.interestservice.dto.response.*;
import com.familring.interestservice.exception.*;
import com.familring.interestservice.repository.InterestAnswerRepository;
import com.familring.interestservice.repository.InterestMissionRepository;
import com.familring.interestservice.repository.InterestRepository;
import com.familring.interestservice.service.client.FamilyServiceFeignClient;
import com.familring.interestservice.service.client.FileServiceFeignClient;
import com.familring.interestservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final FileServiceFeignClient fileServiceFeignClient;

    @Value("${aws.s3.interest-photo-path}")
    private String interestPhotoPath;

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

    // 관심사 체험 인증 기간 설정
    public void setInterestMissionPeriod(Long userId, InterestMissionCreatePeriodRequest interestMissionCreatePeriodRequest) {

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

        // 해당 관심사에 인증 기간 설정 (selected 하나라도 true 인 상태일 때만 가능함)
        boolean hasSelectedAnswer = interestAnswers.stream().anyMatch(InterestAnswer::isSelected);

        LocalDate today = LocalDate.now();
        if (hasSelectedAnswer) {
            // 인증 기간 설정
            if (interest.getMissionEndDate() == null) {
                if (interestMissionCreatePeriodRequest.getEndDate().isAfter(today)) {
                    interest.updateMissionEndDate(interestMissionCreatePeriodRequest.getEndDate());
                } else {
                    throw new InvalidInterestMissionEndDateException();
                }
            } else {
                // 인증 기한이 이미 설정되어 있음
                throw new AlreadyExistInterestMissionEndDateException();
            }

        } else {
            throw new InterestAnswerNotFoundException();
        }

    }

    // 관심사 체험 인증 남은 기간 조회
    public int getInterestMissionDate(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        int diff = 0;
        if (interest.getMissionEndDate() != null) {
            diff = (int) ChronoUnit.DAYS.between(today, interest.getMissionEndDate());
        } else {
            throw new InterestMissionEndDateNotFoundException(); // 관심사 인증 기간을 설정하지 않았을 때
        }

        // 남은 기간 조회
        return diff;
    }

    // 관심사 체험 인증 게시글 작성
    public void createInterestMission(Long userId, MultipartFile image) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        List<MultipartFile> files = new ArrayList<>();
        files.add(image);
        String photoUrl = fileServiceFeignClient.uploadFiles(files, getInterestPhotoPath(familyId)).getData().get(0);

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 오늘
        LocalDate today = LocalDate.now();

        InterestMission interestMission = InterestMission
                .builder()
                .userId(userId)
                .interest(interest)
                .photoUrl(photoUrl)
                .createdAt(today)
                .build();

        interestMissionRepository.save(interestMission);
    }

    private String getInterestPhotoPath(Long familyId) {
        return interestPhotoPath + "/" + familyId;
    }

    // 관심사 체험 인증 목록 조회
    public InterestMissionListResponse getInterestMissionList(Long userId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Interest interest = interestRepository.findFirstByFamilyId(familyId).orElseThrow(InterestNotFoundException::new);

        // 가족 구성원 찾기
        List<UserInfoResponse> familyMembers = familyServiceFeignClient.getFamilyMemberList(userId).getData();

        int count = 0;
        List<InterestMissionItem> interestMissionItemList = new ArrayList<>();
        for (UserInfoResponse member : familyMembers) {
            // 관심사, 회원 번호 찾아서
            Optional<InterestMission> interestMission = interestMissionRepository.findByInterestAndUserId(interest, member.getUserId());

            if (interestMission.isPresent()) {
                count++; // 있으면 count 올리기

                // 이미지, 닉네임
                InterestMissionItem interestMissionItem = InterestMissionItem
                        .builder()
                        .photoUrl(interestMission.get().getPhotoUrl())
                        .userNickname(member.getUserNickname())
                        .build();

                interestMissionItemList.add(interestMissionItem);
            }
        }

        return InterestMissionListResponse
                .builder()
                .items(interestMissionItemList)
                .count(count)
                .build();
    }

    // 관심사 전체 목록 조회
    public InterestListResponse getInterestList(Long userId, int pageNo) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가족이 생성했던 관심사 전체 조회
        PageRequest pageRequest = PageRequest.of(pageNo, 18); // 18개씩
        Slice<Interest> interestSlice = interestRepository.findByFamilyIdOrderByIdDesc(familyId, pageRequest);
        List<Interest> interestList = interestSlice.getContent();

        int totalCount = interestRepository.countByFamilyId(familyId);

        List<InterestItem> interestItemList = new ArrayList<>();

        int index = 0;
        // interestList 돌면서
        for (Interest interest : interestList) {
            // interestAnswerRepository 에서 관심사, 가족 ID로 찾아서 그 중에 select 가 true 인 것만
            Optional<InterestAnswer> interestAnswer = interestAnswerRepository.findSelectedAnswersByFamilyIdAndInterest(familyId, interest);

            if (interestAnswer.isPresent()) {
                UserInfoResponse userInfo = userServiceFeignClient.getUser(interestAnswer.get().getUserId()).getData();
                String userNickname = userInfo.getUserNickname();

                InterestItem interestItem = InterestItem
                        .builder()
                        .interestId(interest.getId())
                        .index(totalCount - (pageNo * 18 + index))
                        .content(interestAnswer.get().getContent())
                        .userNickname(userNickname) // userId로 userNickname 찾아서
                        .build();

                // interestItemList 에 add 해서
                interestItemList.add(interestItem);

                // index 증가
                index++;
            }
        }

        return InterestListResponse
                .builder()
                .items(interestItemList)
                .isLast(interestSlice.isLast())
                .hasNext(interestSlice.hasNext())
                .build();
    }

    // 관심사 상세보기
    public InterestDetailListResponse getInterestDetail(Long userId, Long interestId) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 관심사 고유번호, 가족 고유번호 로 관심사 찾기
        Optional<Interest> interest = interestRepository.findByIdAndFamilyId(interestId, familyId);

        List<InterestDetailItem> interestDetailItemList = new ArrayList<>();
        if (interest.isPresent()) {
            List<InterestMission> interestMissionList = interestMissionRepository.findByInterest(interest.get());

            for (InterestMission interestMission : interestMissionList) {
                String photoUrl = interestMission.getPhotoUrl();

                UserInfoResponse userInfo = userServiceFeignClient.getUser(interestMission.getUserId()).getData();

                InterestDetailItem interestDetailItem = InterestDetailItem
                        .builder()
                        .photoUrl(photoUrl)
                        .userNickname(userInfo.getUserNickname())
                        .userZodiacSign(userInfo.getUserZodiacSign())
                        .build();

                interestDetailItemList.add(interestDetailItem);
            }

        } else {
            throw new InterestNotFoundException();
        }

        return InterestDetailListResponse
                .builder()
                .items(interestDetailItemList)
                .build();
    }

    // 관심사 상태 관리
    public int getInterestStatus(Long userId) {
        int status = 0;

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 관심사 찾기
        Optional<Interest> interestOptional = interestRepository.findFirstByFamilyId(familyId);

        // 0 → 작성하는 기간: 관심사 DB에 아무것도 없을 때 작성 가능한 상태
        if (interestOptional.isEmpty()) {
            return 0;
        }

        Interest interest = interestOptional.get();
        Optional<InterestAnswer> interestAnswer = interestAnswerRepository.findSelectedAnswersByFamilyIdAndInterest(familyId, interest);

        // 0 → 작성하는 기간: 최근 관심사에 선정된 답변이 없고, 인증 기간도 설정되지 않은 경우
        if (interestAnswer.isEmpty() && interest.getMissionEndDate() == null) {
            return 0;
        }

        // 1 → 관심사 선정 완료, 인증 기간 미설정: 선정된 답변이 있으나 인증 기간이 설정되지 않은 경우
        if (interestAnswer.isPresent() && interest.getMissionEndDate() == null) {
            return 1;
        }

        // 2 → 인증 기간: 관심사 선정 완료 & 인증 기간 설정된 경우
        return 2;
    }

}
