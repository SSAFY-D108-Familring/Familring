package com.familring.familyservice.model.dao;

import com.familring.familyservice.model.dto.FamilyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FamilyDao {
    // familyId로 family 찾기
    FamilyDto findFamilyByFamilyId(@Param("familyId") Long familyId);

    // userId로 family 찾기
    FamilyDto findFamilyByUserId(@Param("userId")Long userId);

    // userId로 family_user 모두 찾기
    List<Long> findFamilyUserByUserId(@Param("userId") Long userId);
}
