package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;

import java.util.List;

/**
 * 시설 카테고리 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 편의시설 분류에 사용되는 카테고리를 관리합니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface FacilityCategoryService {

	/**
	 * 모든 시설 카테고리 목록을 조회합니다.
	 *
	 * @return {@code List<FacilityCategoryDTO>} 시설 카테고리 리스트
	 */
	public List<FacilityCategoryDTO> findAll();

	/**
	 * 새로운 카테고리를 생성합니다.
	 *
	 * @param facility 시설 카테고리 정보
	 */
	public void save(FacilityCategoryDTO facility);

	/**
	 * 기존 시설 카테고리 정보를 수정합니다.
	 *
	 * @param facilityId 시설 카테고리 ID
	 * @param facility   수정할 시설 카테고리 정보가 담긴 facility
	 */
	public void update(Long facilityId, FacilityCategoryDTO facility);

	/**
	 * 특정 카테고리를 삭제합니다.
	 *
	 * @param facilityId 삭제할 카테고리의 ID
	 */
	public void delete(Long facilityId);

	/**
	 * 특정 가게(store)에 카테고리를 연결합니다.
	 *
	 * @param storeId    카테고리를 연결할 가게의 ID
	 * @param facilityId 연결될 카테고리의 ID
	 */
	public void addFacilityCategoryToStore(Long storeId, Long facilityId);

	/**
	 * 특정 가게(store)에서 시설 카테고리 연결을 해제합니다.
	 *
	 * @param storeId    시설 카테고리 연결을 해제할 가게의 ID
	 * @param facilityId 연결 해제될 카테고리의 ID
	 */
	public void removeFacilityCategoryFromStore(Long storeId, Long facilityId);
}
