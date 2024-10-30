package com.familring.familyservice.model.dao;

import com.familring.familyservice.model.dto.FamilyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FamilyDao {
    // userId로 family 찾기
    FamilyDto findFamilyInfoByUserId(@Param("userId")Long userId);

    // familyId로 family 찾기
    FamilyDto findFamilyByFamilyId(@Param("familyId") Long familyId);
}
