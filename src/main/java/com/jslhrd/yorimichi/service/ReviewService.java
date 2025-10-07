package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.ReviewDTO;

import java.util.List;

/**
 * 리뷰 관련 서비스 인터페이스 입니다.
 *
 * @author mj2sdev
 * @author LancerAlert
 * @version 1.1 실제 설계도에 맞게 변경
 */
public interface ReviewService {

	/**
	 * 최신 리뷰 리스트를 조회합니다.
	 *
	 * @return {@code List<ReviewDTO>} 리뷰 LIST
	 */
	public List<ReviewDTO> findAll();

	/**
	 * 가게 ID를 기반으로 리뷰를 여러 개 찾습니다.
	 *
	 * @param storeId 가게 ID
	 * @return {@code List<ReviewDTO>} 리뷰 LIST
	 */
	public List<ReviewDTO> findAllByStoreId(Long storeId);

	/**
	 * 리뷰 상세 데이터를 조회합니다.
	 *
	 * @param reviewId 리뷰 ID
	 * @return {@code ReviewDTO} 리뷰 데이터
	 */
	public ReviewDTO findById(Long reviewId);

	/**
	 * Review 작성 데이터를 저장합니다.
	 *
	 * @param userId  유저 ID
	 * @param storeId 상점 ID
	 * @param review  리뷰 데이터
	 */
	public void save(Long userId, Long storeId, ReviewDTO review);

	/**
	 * Review 수정 데이터를 반영합니다.
	 *
	 * @param userId   유저 ID
	 * @param reviewId 리뷰 ID
	 * @param review   리뷰 데이터
	 */
	public void update(Long userId, Long reviewId, ReviewDTO review);


	/**
	 * 리뷰아이디 (reviewId) 를 이용해 리뷰를 삭제합니다.
	 *
	 * @param userId   권한 확인 유저 ID
	 * @param reviewId 삭제할 리뷰 ID
	 */
	public void delete(Long userId, Long reviewId);
}
