package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    int insert(CategoryDTO category); // 카테고리 등록
    CategoryDTO selectById(Long categoryId); // 카테고리 조회
    List<CategoryDTO> listChildrenOf(Long parentCategoryId); // 특정 부모의 하위 카테고리 조회
    int update(CategoryDTO category); // 카테고리 수정
    int deleteById(Long categoryId); // 카테고리 삭제
}
