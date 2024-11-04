package com.familring.familyservice.model.dao;

import com.familring.familyservice.model.dto.FamilyDto;
import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FamilyDao {
    // familyId로 family 찾기
    Optional<FamilyDto> findFamilyByFamilyId(@Param("familyId") Long familyId);

    // userId로 family 찾기
    Optional<FamilyDto> findFamilyByUserId(@Param("userId")Long userId);

    // familyCode로 family 찾기
    Optional<FamilyDto> findFamilyByFamilyCode(String familyCode);

    // userId로 family_user 모두 찾기
    List<Long> findFamilyUserByUserId(@Param("userId") Long userId);

    // 가장 마지막에 입력한 familyId 찾기
    Long findLastInsertedFamilyId();

    // familyCode로 family 유무 확인
    boolean existsFamilyByFamilyCode(@Param("code") String code);

    // familyI와 userId로 가족 구성원 유무 확인
    boolean existsFamilyByFamilyIdAndUserId(@Param("familyId") Long familyId,@Param("userId") Long userId);

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
