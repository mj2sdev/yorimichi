package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FoodCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FoodCategoryMapper {
	int insert(FoodCategoryDTO row); // 조인 등록
	FoodCategoryDTO selectById(@Param("id") Long id); // 단건 조회
	List<FoodCategoryDTO> selectByFoodId(@Param("foodId") Long foodId); // 음식 기준 목록
	List<FoodCategoryDTO> selectByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 목록
	int deleteById(@Param("id") Long id); // 단건 삭제
	int deleteByFoodId(@Param("foodId") Long foodId); // 음식 기준 일괄 삭제
	int deleteByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 일괄 삭제
}
