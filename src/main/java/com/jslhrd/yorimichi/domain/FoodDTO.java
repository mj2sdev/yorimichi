package com.jslhrd.yorimichi.domain;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 음식 DTO.
 *
 * <br>음식 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class FoodDTO extends RootDTO {

	/**
	 * FK: 상점 ID
	 */
	@NotNull
	@Positive
	private Long storeId;

	/**
	 * 음식 이름
	 */
	@NotBlank
	@Size(max = 50)
	private String name;

	/**
	 * 음식 가격
	 */
	@Min(0)
	private Integer price;

	/**
	 * 음식 설명
	 */
	@Size(max = 2000)
	private String description;

	/**
	 * 상점
	 */
	private StoreDTO store;

	/**
	 * 음식 이미지 목록
	 */
	private List<ImageDTO> images;

	/**
	 * 리뷰 목록
	 */
	private List<ReviewDTO> reviews;
}