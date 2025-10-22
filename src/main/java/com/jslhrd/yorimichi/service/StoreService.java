package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;

import java.util.List;

/**
 * 가게 정보관련 서비스 입니다.
 *
 * @author mj2sdev
 * @version 1.0 초안 작성
 * @version 1.1 findById 함수에 userId 추가 -> 유저 정보를 넘겨야 해당 가게 좋아요 상태가 감지가능
 */
public interface StoreService {

	/**
	 * 가게를 검색하는 함수입니다.
	 *
	 * @param store 검색관련 데이터 store 입니다.
	 * @return {@code List<StoreDTO>} 가게 정보 리스트를 반환합니다.
	 */
	public List<StoreDTO> findAll(SearchDTO store);

	/**
	 * 사용자가 좋아요 표시한 가게 정보 리스트를 가져옵니다.
	 *
	 * @param userId 사용자의 아이디 입니다.
	 * @return {@code List<StoreDTO>} 가게 정보 리스트를 반환합니다.
	 */
	public List<StoreDTO> findAllByUserLike(Long userId);

	public List<StoreDTO> findAllByRecommend(int limit);

	/**
	 * 가게 아이디를 이용해 가게정보를 조회합니다.
	 *
	 * @param storeId 가게 아이디
	 * @param userId  유저 아이디
	 * @return {@code StoreDTO} 가게 정보를 반환합니다.
	 * @see StoreDTO
	 */
	public StoreDTO findById(Long storeId, Long userId);

	/**
	 * 가게 정보를 저장하는 함수입니다.
	 *
	 * @param store 가게정보
	 */
	public void save(StoreDTO store);

	/**
	 * 가게 정보를 업데이트 하는 함수입니다.
	 *
	 * @param storeId 가게 아이디
	 * @param store   가게 정보
	 */
	public void update(Long storeId, StoreDTO store);

	/**
	 * 가게 정보를 삭제하는 함수 입니다.
	 *
	 * @param storeId 가게 아이디
	 *
	 */
	public void delete(Long storeId);
}
