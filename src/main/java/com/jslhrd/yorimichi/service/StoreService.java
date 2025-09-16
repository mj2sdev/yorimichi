package com.jslhrd.yorimichi.service;

import java.util.List;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;

/**
 * 가게 정보관련 서비스 입니다.
 */
public interface StoreService {
	
	/**
	 * 가게를 검색하는 함수입니다.
	 * @param dto
	 * @return List<StoreDTO> 가게 정보 리스트를 반환합니다.
	 */
	public List<StoreDTO> findStore(SearchDTO dto);

	/**
	 * 가게 정보를 저장하는 함수입니다.
	 * @param dto
	 */
	public void saveStore(StoreDTO dto);

	/**
	 * 가게 정보를 업데이트 하는 함수입니다.
	 * @param dto
	 */
	public void updateStore(StoreDTO dto);

	/**
	 * 가게 정보를 삭제하는 함수 입니다.
	 * @param storeId
	 */
	public void deleteStore(Long storeId);
}
