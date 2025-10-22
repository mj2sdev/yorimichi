package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SliceResponse;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 가게, 음식, 편의시설 등 다양한 분류에 사용되는 카테고리를 관리합니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface CategoryService {

	/**
	 * 모든 카테고리 목록을 조회합니다.
	 *
	 * @return {@code List<CategoryDTO>} 카테고리 리스트
	 */
	public SliceResponse<CategoryDTO> findSlice(Long categoryId, int size);

	/**
	 * 새로운 카테고리를 생성합니다.
	 *
	 * @param category 카테고리 정보
	 */
	public void save(CategoryDTO category);

	/**
	 * 기존 카테고리 정보를 수정합니다.
	 *
	 * @param categoryId 카테고리 ID
	 * @param category   수정할 카테고리 정보가 담긴 category
	 */
	public void update(Long categoryId, CategoryDTO category);

	/**
	 * 특정 카테고리를 삭제합니다.
	 *
	 * @param categoryId 삭제할 카테고리의 ID
	 */
	public void delete(Long categoryId);

	/**
	 * 특정 가게(store)에 카테고리를 연결합니다.
	 *
	 * @param storeId    카테고리를 연결할 가게의 ID
	 * @param categoryId 연결될 카테고리의 ID
	 */
	public void addCategoryToStore(Long storeId, Long categoryId);

	/**
	 * 특정 가게(store)에서 카테고리 연결을 해제합니다.
	 *
	 * @param storeId    카테고리 연결을 해제할 가게의 ID
	 * @param categoryId 연결 해제될 카테고리의 ID
	 */
	public void removeCategoryFromStore(Long storeId, Long categoryId);
}
