package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 읍/면/동 DTO.
 *
 * <br>읍/면/동 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("RegionEmdDTO")
public class RegionEmdDTO {

	/**
	 * PK: 읍/면/동 ID
	 */
	private Long id;

	/**
	 * FK: 시/군/구 ID
	 */
	private Long sigunguId;

	/**
	 * 읍/면/동 코드
	 */
	private String code;

	/**
	 * 읍/면/동 이름
	 */
	private String name;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}