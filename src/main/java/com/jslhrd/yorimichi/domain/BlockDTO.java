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
 * 차단 DTO.
 *
 * <br>차단 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class BlockDTO {

	/**
	 * FK: 차단한 유저 ID
	 */
	@NotNull
	@Positive
	private Long blockerId;

	/**
	 * FK: 차단당한 유저 ID
	 */
	@NotNull
	@Positive
	private Long blockedId;

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