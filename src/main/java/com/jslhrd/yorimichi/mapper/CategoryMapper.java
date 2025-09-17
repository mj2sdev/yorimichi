package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    int insert(CategoryDTO category);                    // 카테고리 등록
    CategoryDTO selectById(@Param("id") Long id);        // 단건 조회
    int update(CategoryDTO category);                    // 수정
    int deleteById(@Param("id") Long id);               // 삭제

    // 특정 부모의 하위 카테고리 목록
    List<CategoryDTO> listByParentId(@Param("parentId") Long parentId);

    // 최상위 카테고리 목록(리뷰 질문 반영: parent_id IS NULL 전용)
    List<CategoryDTO> listRoots();
}
