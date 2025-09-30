package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 루트 DTO.
 *
 * <br>루트 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class RootDTO {

	/**
	 * PK: 루트 ID
	 */
	private Long id;

	/**
	 * 루트 타입 (USER, STORE, FOOD, COEAT, COMMENT, REVIEW)
	 */
	private RootType type;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;

	/**
	 * 삭제일시
	 */
	private LocalDateTime deletedAt;

	/**
	 * 숨김일시
	 */
	private LocalDateTime blindedAt;
}