package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 신고 DTO.
 *
 * <br>신고 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("ReportDTO")
public class ReportDTO {

	/**
	 * FK: 신고자 ID
	 */
	private Long reporterId;

	/**
	 * FK: 루트 ID
	 */
	private Long rootId;

	/**
	 * 신고 사유
	 */
	private String reason;

	/**
	 * 신고 상태
	 */
	private String status;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;
}