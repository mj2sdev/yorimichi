package com.jslhrd.yorimichi.domain;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
	@NotNull
	@Positive
	private Long userId;

	/**
	 * FK: 음식 ID
	 */
	@NotNull
	@Positive
	private Long foodId;

	/**
	 * 리뷰 평점
	 */
	@Min(1)
	@Max(5)
	private Integer rating;

	/**
	 * 리뷰 내용
	 */
	@Size(max = 5000)
	private String content;


	/**
	 * 리뷰 작성한 유저
	 */
	private UserDTO user;

	/**
	 * 리뷰 작성된 상점
	 */
	private StoreDTO store;

	/**
	 * 리뷰 음식 목록
	 */
	private List<FoodDTO> foods;

	/**
	 * 리뷰 이미지 목록
	 */
	private List<ImageDTO> images;
}