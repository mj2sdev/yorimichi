package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 시/도 DTO.
 *
 * <br>시/도 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class RegionSidoDTO {

	/**
	 * FK: 시/도 ID
	 */
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * 시/도 코드
	 */
	@NotBlank
	@Size(max = 10)
	private String code;

	/**
	 * 시/도 이름
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
	 * 시/군/읍 목록
	 */
	private List<RegionSigunguDTO> sigungus;
}