package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;

import java.util.List;

/**
 * 가게 정보관련 서비스 입니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface StoreService {

	/**
	 * 가게를 검색하는 함수입니다.
	 *
	 * @param dto 검색관련 데이터 DTO 입니다.
	 * @return {@code List<StoreDTO>} 가게 정보 리스트를 반환합니다.
	 */
	public List<StoreDTO> findAll(SearchDTO dto);

	/**
	 * 사용자가 좋아요 표시한 가게 정보 리스트를 가져옵니다.
	 *
	 * @param userId 사용자의 아이디 입니다.
	 * @return {@code List<StoreDTO>} 가게 정보 리스트를 반환합니다.
	 */
	public List<StoreDTO> findAllByUserLike(Long userId);

	/**
	 * 사용자의 아이디를 받아서 사용자의 정보를 토대로 추천하는 가게 리스트를 반환합니다.
	 * userId 값이 null 일 경우에는 일반적인 추천 게시물(예를들어 인기, 최근 평점 등) 을 대신 반환합니다.
	 *
	 * @param userId 사용자의 아이디 입니다. 필수 아님
	 * @return {@code List<StoreDTO>} 추천 가게 리스트를 반환합니다.
	 */
	public List<StoreDTO> findAllByRecommend(Long userId);

	/**
	 * 가게 아이디를 이용해 가게정보를 조회합니다.
	 *
	 * @param storeId 가게 아이디
	 * @return {@code StoreDTO} 가게 정보를 반환합니다.
	 */
	public StoreDTO findById(Long storeId);

	/**
	 * 가게 정보를 저장하는 함수입니다.
	 *
	 * @param dto 가게정보
	 */
	public void save(StoreDTO dto);

	/**
	 * 가게 정보를 업데이트 하는 함수입니다.
	 *
	 * @param storeId 가게 아이디
	 * @param dto     가게 정보
	 */
	public void update(Long storeId, StoreDTO dto);

	/**
	 * 가게 정보를 삭제하는 함수 입니다.
	 *
	 * @param storeId 가게 아이디
	 *
	 */
	public void delete(Long storeId);
}
