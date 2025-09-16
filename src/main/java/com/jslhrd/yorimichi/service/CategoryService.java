package com.jslhrd.yorimichi.service;

import java.util.List;
import com.jslhrd.yorimichi.domain.CategoryDTO;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 가게, 음식, 편의시설 등 다양한 분류에 사용되는 카테고리를 관리합니다.
 * @author MJ2
 * @since 2025.09.16
 */
public interface CategoryService {

	/**
	 * 새로운 카테고리를 생성합니다.
	 * @param dto 생성할 카테고리 정보가 담긴 DTO
	 */
	void saveCategory(CategoryDTO dto);

	/**
	 * 기존 카테고리 정보를 수정합니다.
	 * @param dto 수정할 카테고리 정보가 담긴 DTO
	 */
	void updateCategory(CategoryDTO dto);

	/**
	 * 특정 카테고리를 삭제합니다.
	 * @param categoryId 삭제할 카테고리의 ID
	 */
	void deleteCategory(Long categoryId);

	/**
	 * 모든 카테고리 목록을 조회합니다.
	 * @return 카테고리 정보(CategoryDTO) 리스트
	 */
	List<CategoryDTO> findAllCategories();

	/**
	 * 특정 가게(store)에 카테고리를 연결합니다.
	 * @param storeId 카테고리를 연결할 가게의 ID
	 * @param categoryId 연결될 카테고리의 ID
	 */
	void linkCategoryToStore(Long storeId, Long categoryId);

	/**
	 * 특정 가게(store)에서 카테고리 연결을 해제합니다.
	 * @param storeId 카테고리 연결을 해제할 가게의 ID
	 * @param categoryId 연결 해제될 카테고리의 ID
	 */
	void unlinkCategoryFromStore(Long storeId, Long categoryId);
	
}
