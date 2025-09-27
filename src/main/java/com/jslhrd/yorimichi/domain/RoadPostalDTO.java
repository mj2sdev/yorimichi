package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 도로명 - 우편번호 매핑 DTO.
 *
 * <br>도로명 - 우편번호 매핑 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class RoadPostalDTO {

	/**
	 * FK: 도로명 ID
	 */
	@NotNull
	@Positive
	private Long roadId;

	/**
	 * FK: 우편번호 ID
	 */
	@NotNull
	@Positive
	private Long postalId;

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