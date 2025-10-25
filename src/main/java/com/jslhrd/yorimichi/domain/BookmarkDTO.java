package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 북마크(즐겨찾기) DTO.
 *
 * <br>북마크(즐겨찾기) 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class BookmarkDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long userId;

	/**
	 * FK: 상점 ID
	 */
	private Long storeId;

	private Integer count;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}