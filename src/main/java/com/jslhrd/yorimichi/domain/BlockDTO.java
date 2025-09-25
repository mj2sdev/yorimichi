package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
@Alias("BlockDTO")
public class BlockDTO {

	/**
	 * FK: 차단한 유저 ID
	 */
	private Long blockerId;

	/**
	 * FK: 차단당한 유저 ID
	 */
	private Long blockedId;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}