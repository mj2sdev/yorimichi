package com.jslhrd.yorimichi.service;

import java.security.Principal;
import java.util.List;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;

import java.util.List;

/**
 * 리뷰 관련 서비스 인터페이스 입니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface ReviewService {

	/**
	 * 최신 리뷰 리스트를 조회합니다.
	 *
	 * @return review list
	 */
	public List<ReviewDTO> findAll();

	/**
	 * 가게 아이디를 기준으로 리뷰 리스트를 조회합니다.
	 *
	 * @param storeId 가게 아이디
	 * @return review list
	 */
	public List<ReviewDTO> findAllByStoreId(Long storeId);

	/**
	 * 리뷰 상세 데이터를 조회합니다.
	 *
	 * @param reviewId 리뷰 아이디
	 * @return {@code ReviewDTO} 리뷰 데이터
	 */
	public ReviewDTO findById(Long reviewId);

	/**
	 * 부적절한 내용의 리뷰를 신고합니다.
	 *
	 * @param dto 신고 대상, 신고자, 기타등등 데이터 필요
	 */
	public void report(ReportDTO dto);

	/**
	 * Review 작성 데이터를 저장합니다.
	 *
	 * @param userId  리뷰 작성 유저 ID
	 * @param storeId 리뷰 작성 상점 ID
	 * @param dto     리뷰 데이터
	 */
	public void save(Long userId, Long storeId, ReviewDTO dto);

	/**
	 * Review 수정 데이터를 반영합니다.
	 *
	 * @param reviewId 수정할 리뷰 ID
	 * @param userId   권한 확인 유저 ID
	 * @param dto      수정된 리뷰 데이터
	 */
	public void update(Long reviewId, Long userId, ReviewDTO dto);


	/**
	 * 리뷰아이디 (reviewId) 를 이용해 리뷰를 삭제합니다.
	 * TODO: 컨트롤러의 임시 작성을 위해 principal을 추가했습니다. 검토 바랍니다.
	 *
	 * @param reviewId 삭제할 리뷰 ID
	 * @param userId   권한 확인 유저 ID
	 */
	public void delete(Long reviewId, Long userId);

}
