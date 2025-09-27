package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
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
	@NotNull(groups = Create.class)
	@Positive(groups = {Create.class, Update.class})
	private Long addressId;

	/**
	 * 상점 이름
	 */
	@NotBlank
	@Size(max = 100)
	private String name;

	/**
	 * 상점 설명
	 */
	@Size(max = 1000)
	private String description;

	/**
	 * 상점 번호
	 */
	@Size(max = 20)
	@Pattern(regexp = "^[+0-9][0-9\\- ]{6,19}$",
			message = "전화번호 형식이 올바르지 않습니다.")
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
}