package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 카테고리 DTO.
 *
 * <br>카테고리 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class CategoryDTO {

	/** PK: 카테고리 ID */
    private Long id;

    /** FK: 부모 카테고리 ID */
    private Long parentId;
    
    /** 카테고리 이름 */
    private String name;

    /** 생성일시 (DB 자동 생성) */
    private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;
}