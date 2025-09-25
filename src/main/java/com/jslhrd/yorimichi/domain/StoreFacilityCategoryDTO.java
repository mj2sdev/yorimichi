package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 상점 - 시설 카테고리 매핑 DTO.
 *
 * <br>상점 - 시설 카테고리 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class StoreFacilityCategoryDTO {

	/** FK: 상점 ID */
    private Long storeId;

	/** FK: 시설 카테고리 ID */
    private Long facilityCategoryId;

	/** 생성일시 (DB 자동 생성) */
	private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;
}