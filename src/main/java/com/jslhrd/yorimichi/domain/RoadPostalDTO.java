package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
@Alias("RoadPostalDTO")
public class RoadPostalDTO {

	/**
	 * FK: 도로명 ID
	 */
	private Long roadId;

	/**
	 * FK: 우편번호 ID
	 */
	private Long postalId;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}