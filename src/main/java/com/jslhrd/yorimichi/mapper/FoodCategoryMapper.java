package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FoodCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FoodCategoryMapper {
    int insert(FoodCategoryDTO foodCategory); // 매핑 추가
    int delete(FoodCategoryDTO foodCategory); // 매핑 삭제
    int count(FoodCategoryDTO foodCategory); // 매핑 존재 여부
    List<FoodCategoryDTO> listByFoodId(Long foodId); // 특정 음식의 카테고리 목록
    List<FoodCategoryDTO> listByCategoryId(Long categoryId); // 특정 카테고리에 속한 음식매핑 목록
}
