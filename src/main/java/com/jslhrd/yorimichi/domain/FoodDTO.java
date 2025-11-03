package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString(callSuper = true)
public class FoodDTO extends RootDTO {

	/**
	 * FK: 상점 ID
	 */
	private Long storeId;

	/**
	 * 음식 이름
	 */
	private String name;

	/**
	 * 음식 가격
	 */
	private Integer price;

	/**
	 * 음식 설명
	 */
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

	public FoodDTO() {
		super(RootType.FOOD);
	}
}