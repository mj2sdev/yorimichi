package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 시/군/구 DTO.
 *
 * <br>시/군/구 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class RegionSigunguDTO {

	/**
	 * PK: 시/군/구 ID
	 */
	private Long id;

	/**
	 * FK: 시/도 ID
	 */
	private Long sidoId;

	/**
	 * 시/군/구 코드
	 */
	private String code;

	/**
	 * 시/군/구 이름
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


	/**
	 * 시/도
	 */
	private RegionSidoDTO regionSidoDTO;

	/**
	 * 읍/면/동 목록
	 */
	private List<RegionEmdDTO> regionEmds;
}