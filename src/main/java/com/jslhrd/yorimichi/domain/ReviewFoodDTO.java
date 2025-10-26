package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 리뷰 - 음식 매핑 DTO.
 *
 * <br>리뷰 - 음식 매핑 정보를 전달합니다.
 *
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class ReviewFoodDTO {

	/**
	 * FK: 리뷰 ID
	 */
	private Long reviewId;

	/**
	 * FK: 음식 ID
	 */
	private Long foodId;

	private String foodName;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}