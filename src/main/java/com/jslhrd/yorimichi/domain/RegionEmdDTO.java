package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
public class RegionEmdDTO {

	/**
	 * PK: 읍/면/동 ID
	 */
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * FK: 시/군/구 ID
	 */
	@NotNull
	@Positive
	private Long sigunguId;

	/**
	 * 읍/면/동 코드
	 */
	@NotBlank
	@Size(max = 10)
	private String code;

	/**
	 * 읍/면/동 이름
	 */
	@NotBlank
	@Size(max = 50)
	private String name;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime updatedAt;


	/**
	 * 시/군/구
	 */
	private RegionSigunguDTO regionSigungu;

	/**
	 * 도로명 목록
	 */
	private List<RoadDTO> roads;
}