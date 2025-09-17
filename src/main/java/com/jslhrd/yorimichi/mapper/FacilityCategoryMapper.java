package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FacilityCategoryMapper {
	int insert(FacilityCategoryDTO row); // 조인 등록
	FacilityCategoryDTO selectById(@Param("id") Long id); // 단건 조회
	List<FacilityCategoryDTO> selectByFacilityId(@Param("facilityId") Long facilityId); // 시설 기준 목록
	List<FacilityCategoryDTO> selectByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 목록
	int deleteById(@Param("id") Long id); // 단건 삭제
	int deleteByFacilityId(@Param("facilityId") Long facilityId); // 시설 기준 일괄 삭제
	int deleteByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 일괄 삭제
}
