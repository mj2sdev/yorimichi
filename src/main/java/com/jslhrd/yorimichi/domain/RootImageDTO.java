package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 루트 - 이미지 매핑 DTO.
 *
 * <br>루트 - 이미지 매핑 정보를 잔달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("RootImageDTO")
public class RootImageDTO {

	/**
	 * FK: 루트 ID
	 */
	private Long rootId;

	/**
	 * FK: 이미지 ID
	 */
	private Long imageId;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}