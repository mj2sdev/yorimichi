package com.jslhrd.yorimichi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodCategoryMapper {
	int insert(@Param("foodId") Long foodId,@Param("categoryId") Long categoryId); // 조인 등록
	int deleteByFoodIdAndCategoryId(@Param("foodId") Long foodId,@Param("categoryId") Long categoryId);
	
}
