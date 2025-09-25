package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 시설 카테고리 DTO.
 *
 * <br>시설 카테고리 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("FacilityCategoryDTO")
public class FacilityCategoryDTO {

	/**
	 * PK: 시설 카테고리 ID
	 */
	private Long id;

	/**
	 * 시설 카테고리 이름
	 */
	private String name;

	/**
	 * 생성시각 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}