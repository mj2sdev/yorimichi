package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 상점 DTO.
 *
 * <br>상점 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class StoreDTO extends RootDTO {

	/**
	 * FK: 주소 ID
	 */
	private Long addressId;

	/**
	 * 상점 ID
	 */
	private Long storeId;

	/**
	 * 상점 이름
	 */
	private String name;

	/**
	 * 상점 설명
	 */
	private String description;

	/**
	 * 상점 번호
	 */
	private String phone;

	/**
	 * 주소
	 */
	private AddressDTO address;

	/**
	 * 음식 카테고리  목록
	 */
	private List<CategoryDTO> categories;

	/**
	 * 시설 카테고리 목록
	 */
	private List<FacilityCategoryDTO> facilities;

	/**
	 * 음식 목록
	 */
	private List<FoodDTO> foods;

	/**
	 * 리뷰 목록
	 */
	private List<ReviewDTO> reviews;

	/**
	 * 같이 먹기 목록
	 */
	private List<CoeatDTO> coeats;

	/**
	 * 상점 이미지 목록
	 */
	private List<ImageDTO> images;


	public StoreDTO() {
		super(RootType.STORE);
	}
}
