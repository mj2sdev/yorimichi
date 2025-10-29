package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 - 음식 매핑 DTO.
 *
 * <br>리뷰 - 음식 매핑 정보를 전달합니다.
 *
 * @author LancerAlert
 * @since 1.0
 * 
 * @authow mj2sdev
 * @version 1.1
 * 미춰버리겠네 ^^
 */
@Setter
@Getter
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