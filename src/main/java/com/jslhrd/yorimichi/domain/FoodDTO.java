package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
@Alias("FoodDTO")
public class FoodDTO extends RootDTO {

	/**
	 * PK: 음식 ID
	 */
	private Long id;

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
}