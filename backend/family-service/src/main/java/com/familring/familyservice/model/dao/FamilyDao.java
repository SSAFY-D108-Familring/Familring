package com.familring.familyservice.model.dao;

import com.familring.familyservice.model.dto.Family;
import com.familring.familyservice.model.dto.FamilyRole;
import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FamilyDao {
    // familyId로 family 찾기
    Optional<Family> findFamilyByFamilyId(@Param("familyId") Long familyId);

    // userId로 family 찾기
    Optional<Family> findFamilyByUserId(@Param("userId")Long userId);

    // familyCode로 family 찾기
    Optional<Family> findFamilyByFamilyCode(String familyCode);

    // userId로 family_user 모두 찾기
    List<Long> findFamilyUserByUserId(@Param("userId") Long userId);

    // 가장 마지막에 입력한 familyId 찾기
    Long findLastInsertedFamilyId();

    // 모든 가족 조회
    List<Long> findFamilyId();

    // 가족 구성원 수 조회
    int countFamilyUserByUserId(@Param("userId") Long userId);

    // familyCode로 family 유무 확인
    boolean existsFamilyByFamilyCode(@Param("code") String code);

    // familyId와 userId로 가족 구성원 유무 확인
    boolean existsFamilyByFamilyIdAndUserId(@Param("familyId") Long familyId,@Param("userId") Long userId);

    // familyId로 family 구성원 중 엄마, 아빠 역할이 이미 있는지 확인
    boolean existsFamilyRoleIsMOrFByFamilyId(@Param("familyId") Long familyId, @Param("userRole")FamilyRole role);

    // family 생성
    void insertFamily(FamilyCreateRequest familyCreateRequest);

    // family_user 구성원 추가
    void insetFamily_User(@Param("familyId") Long familyId, @Param("userId") Long userId);

    // 가족 구성원 수 + 1
    void updateFamilyCountByFamilyId(@Param("familyId") Long familyId, @Param("amount") int amount);

    // family_user 구성원 제거
    void deleteFamily_UserByFamilyIdAndUserId(@Param("familyId") Long familyId, @Param("userId") Long userId);

    // familyI에 해당하는 family의 familyCommuniStatus + mount
    void updateFamilyCommunicationStatusByFamilyId(@Param("familyId") Long familyId, @Param("amount") int amount);
}
