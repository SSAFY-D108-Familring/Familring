package com.familring.familyservice.service.family;

import com.familring.common_module.dto.BaseResponse;
import com.familring.familyservice.exception.family.AlreadyFamilyRoleException;
import com.familring.familyservice.exception.family.AlreadyInFamilyException;
import com.familring.familyservice.exception.family.FamilyNotFoundException;
import com.familring.familyservice.model.dao.FamilyDao;
import com.familring.familyservice.model.dto.Family;
import com.familring.familyservice.model.dto.FamilyRole;
import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.request.FamilyStatusRequest;
import com.familring.familyservice.model.dto.request.PersonAlbumCreateRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.client.AlbumServiceFeignClient;
import com.familring.familyservice.service.client.QuestionServiceFeignClient;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FamilyServiceImpl implements FamilyService {

    private final FamilyDao familyDao;
    private final UserServiceFeignClient userServiceFeignClient;
    private final QuestionServiceFeignClient questionServiceFeignClient;
    private final AlbumServiceFeignClient albumServiceFeignClient;

    @Override
    public FamilyInfoResponse getFamilyInfo(Long userId) {
        // 1. 가족 조회
        Family family = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(family.getFamilyId())
                .familyCode(family.getFamilyCode())
                .familyCount(family.getFamilyCount())
                .familyCommunicationStatus(family.getFamilyCommunicationStatus())
                .build();

        // 3. 응답
        return response;
    }

    @Override
    public String getFamilyCode(Long userId) {
        // 1. 가족 조회
        Family family = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());

        // 2.  가족 코드 조회
        String familyCode = family.getFamilyCode();

        // 3. 응답
        return familyCode;
    }

    @Override
    public List<UserInfoResponse> getFamilyMemberList(Long userId) {
        // 1.  userId의 가족 구성원 모두의 userId 추출
        List<Long> members = familyDao.findFamilyUserByUserId(userId);

        // 2. 가족 구성원 userId에 대해 user-service에게 사용자 정보 조회(GET "/users/info")  api 요청
        List<UserInfoResponse> userInfoResponses = userServiceFeignClient.getAllUser(members).getData();

        // 3. 응답
        return userInfoResponses;
    }

    @Override
    public List<UserInfoResponse> getFamilyMemberListByFamilyId(Long familyId) {
        // 1.  userId의 가족 구성원 모두의 userId 추출
        List<Long> members = familyDao.findFamilyUserByFamilyId(familyId);

        // 2. 가족 구성원 userId에 대해 user-service에게 사용자 정보 조회(GET "/users/info")  api 요청
        BaseResponse<List<UserInfoResponse>> response = userServiceFeignClient.getAllUser(members);
        List<UserInfoResponse> userInfoResponses = response.getData(); // data 필드에 접근

        // 3. 응답
        return userInfoResponses;
    }

    @Override
    public boolean validateFamilyCode(String familyCode) {
        return familyDao.existsFamilyByFamilyCode(familyCode);
    }

    @Override
    public List<String> validFamilyMember(String familyCode) {
        List<String> response = new ArrayList<>();

        // 1. 가족 찾기
        Family family = familyDao.findFamilyByFamilyCode(familyCode)
                .orElseThrow(() -> new FamilyNotFoundException());
        log.info("[validFamilyMember] 찾은 가족 familyId={}", family.getFamilyId());

        // 2. 가족 구성원 역할 찾기
        // 2-1. 해당 가족의 구성원 찾기
        List<Long> members = familyDao.findFamilyUserByFamilyId(family.getFamilyId());
        log.info("[validFamilyMember] 가족 구성원 userIds={}", members);

        // 2-2. 가족 구성원의 사용자 정보 조회
        List<UserInfoResponse> userInfoResponses = userServiceFeignClient.getAllUser(members).getData();

        // 2-3. 사용자 정보 중 userRole이 M이나 F찾기
        for (UserInfoResponse user : userInfoResponses) {
            log.info("[validFamilyMember] 가족 구성원 역할 userRole={}", user.getUserRole());
            if (user.getUserRole().equals(FamilyRole.F))
                response.add("아빠");
            else if (user.getUserRole().equals(FamilyRole.M))
                response.add("엄마");
        }

        log.info("[validFamilyMember] 응답 response={}", response);
        return response;
    }

    @Override
    public List<Long> getAllFamilyId() {
        List<Long> response = familyDao.findFamilyId();
        return response;
    }

    @Override
    public int getAllFamilyCount(Long userId) {
        return familyDao.countFamily_UserByUserId(userId);
    }


    @Override
    @Transactional
    public FamilyInfoResponse createFamily(Long userId) {
        // 1.  가족 생성
        // 1-1. 가족 코드 생성
        String code;
        do {
            String uuid = UUID.randomUUID().toString().toUpperCase();
            log.info("UUID: {}", uuid);
            code = uuid.substring(0, 6);
            log.info("code: {}", code);
        }
        // 1-2. 중복 확인
        while (familyDao.existsFamilyByFamilyCode(code));

        // 1-3. dto 수정
        FamilyCreateRequest familyCreateRequest = FamilyCreateRequest.builder()
                .familyCode(code)
                .familyCount(1)
                .familyCommunicationStatus(75)
                .build();

        // 1-4. DB에 가족 생성
        familyDao.insertFamily(familyCreateRequest);
        log.info("가족 생성 완료");

        // 1-5. 생성된 가족 familyId 찾기
        Long familyId = familyDao.findLastInsertedFamilyId();
        log.info("familyId: {}", familyId);

        // 2. 가족 구성원 추가
        familyDao.insetFamily_User(familyId, userId);
        log.info("가족 구성원 추가 완료");

        // 3. 가족 조회
        Family family = familyDao.findFamilyByFamilyId(familyId)
                .orElseThrow(() -> new FamilyNotFoundException());
        log.info("가족 조회 완료");

        // 4. 가족 랜덤 질문 생성
        questionServiceFeignClient.initializeQuestionFamily(familyId);

        // 5. 가족 구성원 인물 앨범 추가
        albumServiceFeignClient.createPersonAlbum(PersonAlbumCreateRequest
                .builder().familyId(familyId).userId(userId).build());

        // 6. 응답 변환
        FamilyInfoResponse response = FamilyInfoResponse.builder()
                .familyId(family.getFamilyId())
                .familyCode(family.getFamilyCode())
                .familyCount(family.getFamilyCount())
                .familyCommunicationStatus(family.getFamilyCommunicationStatus())
                .build();

        // 7. 응답
        return response;
    }

    @Override
    @Transactional
    public String joinFamilyMember(Long userId, FamilyJoinRequest familyJoinRequest) {
        // 1. 가족 찾기
        Family family = familyDao.findFamilyByFamilyCode(familyJoinRequest.getFamilyCode())
                .orElseThrow(() -> new FamilyNotFoundException());
        log.info("familyId: {}", family.getFamilyId());

        // 2. 사용자 정보 찾기
        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.info("userRole: {}", user.getUserRole());

        // 3. 에러 확인
        // 3-1. 가족에 이미 추가 되어있는 경우 에러 발생
        if (familyDao.existsFamilyByFamilyIdAndUserId(family.getFamilyId(), userId)) {
            throw new AlreadyInFamilyException();
        }

        // 3-2. 가족에 엄마, 아빠 역할이 이미 있는 경우 에러 발생
        List<UserInfoResponse> familyMembers = new ArrayList<>();
        if (user.getUserRole().equals(FamilyRole.F)) { // 참여자 가족 역할이 F인 경우
            familyMembers = getFamilyMemberList(family.getFamilyId());
            for (UserInfoResponse member : familyMembers) {
                if (FamilyRole.F.equals(member.getUserRole())) {
                    log.info("userId: {}, role: {}", member.getUserId(), member.getUserRole());
                    throw new AlreadyFamilyRoleException();
                }
            }
        } else if (user.getUserRole().equals(FamilyRole.M)) { // 참여자 가족 역할이 M인 경우
            familyMembers = getFamilyMemberList(family.getFamilyId());
            for (UserInfoResponse member : familyMembers) {
                if (FamilyRole.M.equals(member.getUserRole())) {
                    log.info("userId: {}, role: {}", member.getUserId(), member.getUserRole());
                    throw new AlreadyFamilyRoleException();
                }
            }
        }

        // 4-1. 가족에 추가
        familyDao.insetFamily_User(family.getFamilyId(), userId);
        log.info("가족 추가 완료");

        // 4-2. 가족 구성원 수 + 1
        log.info("before 가족 구성원 수: {}", family.getFamilyCount());
        familyDao.updateFamilyCountByFamilyId(family.getFamilyId(), 1);

        // 5. 인물 앨범 생성
        albumServiceFeignClient.createPersonAlbum(PersonAlbumCreateRequest
                .builder().familyId(family.getFamilyId()).userId(userId).build());

        // 6. 응답
        return "가죽 구성원 추가 완료";
    }

    @Override
    @Transactional
    public String deleteFamilyMember(Long userId) {
        // 1. 회원에 해당하는 가족 찾기
        Family family = familyDao.findFamilyByUserId(userId)
                .orElseThrow(() -> new FamilyNotFoundException());
        log.info("familyId: {}", family.getFamilyId());

        // 2. 가족 구성원 제거
        // 2-1. 가족 구성원 수 - 1
        log.info("before familyCount: {}", family.getFamilyCount());
        familyDao.updateFamilyCountByFamilyId(family.getFamilyId(), -1);

        // 2-2. family_user의 컬럼 삭제
        familyDao.deleteFamily_UserByFamilyIdAndUserId(family.getFamilyId(), userId);
        log.info("family_user의 컬럼 삭제 완료");

        return "가족 구성원 수정 완료";
    }

    @Override
    @Transactional
    public void updateFamilyStatus(FamilyStatusRequest familyStatusRequest) {
        // 1. 가족 찾기
        Family family = familyDao.findFamilyByFamilyId(familyStatusRequest.getFamilyId())
                .orElseThrow(() -> new FamilyNotFoundException());
        int currentStatus = family.getFamilyCommunicationStatus();
        log.info("[updateFamilyStatus] 이전 가족 상태: {}", currentStatus);

        // 2. 가족 상태 변경
        int newStatus = Math.max(0, Math.min(100, currentStatus + familyStatusRequest.getAmount()));
        familyDao.updateFamilyCommunicationStatusByFamilyId(familyStatusRequest.getFamilyId(), newStatus);
        log.info("[updateFamilyStatus] 이후 가족 상태: {}", newStatus);
    }
}
