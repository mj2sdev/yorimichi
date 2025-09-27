package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
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

	/**
	 * PK: 카테고리 ID
	 */
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * FK: 부모 카테고리 ID
	 */
	@Positive
	private Long parentId;

	/**
	 * 카테고리 이름
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
}