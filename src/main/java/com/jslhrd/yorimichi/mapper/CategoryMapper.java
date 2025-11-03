package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 Mapper.
 *
 * <br>카테고리 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CategoryMapper {

	List<CategoryDTO> selectSlice(@Param("categoryId") Long categoryId,
	                              @Param("size") int size);

	/**
	 * 카테고리 단건 조회.
	 *
	 * @param categoryId 카테고리 categoryId
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<CategoryDTO> selectById(@Param("categoryId") Long categoryId);

	Long selectIdByParentAndName(@Param("parentId") Long parentId,
	                             @Param("name") String name);

	boolean existsById(@Param("categoryId") Long categoryId);

	/**
	 * 카테고리 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(CategoryDTO category);

	/**
	 * 카테고리 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("categoryId") Long categoryId,
	           @Param("category") CategoryDTO category);

	/**
	 * 카테고리 삭제.
	 *
	 * @param categoryId 카테고리 categoryId
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("categoryId") Long categoryId);
}