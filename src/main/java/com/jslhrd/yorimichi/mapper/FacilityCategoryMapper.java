package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FacilityCategoryMapper {
	int insert(FacilityCategoryDTO dto); // 조인 등록
	FacilityCategoryDTO selectById(@Param("id") Long id); // 단건 조회
	int update(FacilityCategoryDTO dto);
	int deleteById(@Param("id") Long id);    
	/* 2차 수정 : DTO 이름 통일, CategoryMapper와 통일 */
}
