package com.jslhrd.yorimichi.service;

import java.util.List;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;

/**
 * @author MJ2
 * @since 2025.09.17
 * @note 리뷰 관련 서비스 인터페이스 입니다.
 */
public interface ReviewService {

	/**
	 * 리뷰 리스트를 조회합니다.
	 * @param SearchDTO
	 * @return review list
	 */
	public List<ReviewDTO> findReviews(SearchDTO dto);
	
	/**
	 * Review 작성 데이터를 저장합니다.
	 * @param dto
	 */
	public void saveReview(ReviewDTO dto);
	/**
	 * Review 수정 데이터를 반영합니다.
	 * @param dto
	 */
	public void updateReview(ReviewDTO dto);
	
	/**
	 * 리뷰아이디 (reviewId) 를 이용해 리뷰를 삭제합니다.
	 * @param reviewId
	 */
	public void deleteReview(Long reviewId);
	
}
