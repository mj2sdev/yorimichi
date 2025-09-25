package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰 DTO.
 *
 * <br>리뷰 정보를 전닿합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class ReviewDTO extends RootDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long userId;

	/**
	 * FK: 음식 ID
	 */
	private Long foodId;

	/**
	 * 리뷰 평점
	 */
	private Integer rating;

	/**
	 * 리뷰 내용
	 */
	private String content;
}